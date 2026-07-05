package in.AY.Movie.Backend.Download.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import in.AY.Movie.Backend.Download.Payload.DownloadProgressDto;
import in.AY.Movie.Backend.Download.Payload.DownloadResponseDto;

@Service
public class DownloadService {
	private static final String DOWNLOAD_DIR = "C:/Users/ACER/Downloads/down";
	private volatile String savedQuality = null;
	private volatile DownloadProgressDto currentProgress =
            new DownloadProgressDto();
	private volatile Process currentProcess; 
	private volatile boolean isCancelled = false;
	private volatile String currentFilePath = null;  // ← track the file path
	private volatile boolean isPaused = false;
	private volatile String savedOutputTemplate = null;
	private volatile String savedUrl = null;
	private final Object processLock = new Object();
	
	
	private DownloadResponseDto startDownloadInternal(String url, String outputTemplate, String formatSelector) {
	    isCancelled = false;
	    isPaused = false;
	    savedUrl = url;
	    savedOutputTemplate = outputTemplate;

	    Thread downloadThread = new Thread(() -> {
	        try {
	            ProcessBuilder pb = new ProcessBuilder(
	                /*"C:\\python\\Scripts\\yt-dlp.exe",
	                "-f", "best[height<=1080]",
	                "-o", outputTemplate,
	                url*/
	            		"C:\\python\\Scripts\\yt-dlp.exe",
	            	    "-f", formatSelector,
	            	    "--retries", "infinite",
	            	    "--fragment-retries", "infinite",
	            	    "--retry-sleep", "5",
	            	    "-o", outputTemplate,
	            	    url
	            );
	            pb.redirectErrorStream(true);

	            synchronized (processLock) {
	                currentProcess = pb.start();   // ← assignment now synchronized
	            }

	            BufferedReader reader = new BufferedReader(
	                new InputStreamReader(currentProcess.getInputStream())
	            );

	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (isCancelled) {
	                    deleteCurrentFile();
	                    break;
	                }
	                if (isPaused) {
	                    break; // process already killed by pauseDownload(), just stop reading
	                }

	                System.out.println(line);

	                if (line.contains("Got error") 
	                	    || line.contains("Retrying") 
	                	    || line.contains("Unable to download webpage")
	                	    || line.contains("getaddrinfo failed")
	                	    || line.contains("Network is unreachable")
	                	    || line.contains("Temporary failure in name resolution")) {
	                	    
	                	    currentProgress.setEta("Reconnecting...");  // ← just text, nothing else happens
	                	    continue;     
	                }

	                if (line.contains("[download]") && line.contains("%")) {
	                    try {
	                        Pattern pattern = Pattern.compile(
	                            "(\\d+\\.?\\d*)%.*?of\\s+(.*?)\\s+at\\s+(.*?)\\s+ETA\\s+(.*)"
	                        );
	                        Matcher matcher = pattern.matcher(line);

	                        if (matcher.find()) {
	                            double percentage = Double.parseDouble(matcher.group(1));
	                            String sizeText = matcher.group(2).trim();

	                            String unit;
	                            if (sizeText.contains("GiB"))      unit = "GiB";
	                            else if (sizeText.contains("MiB")) unit = "MiB";
	                            else if (sizeText.contains("KiB")) unit = "KiB";
	                            else                                unit = "";

	                            double total = Double.parseDouble(
	                                sizeText.replace("GiB","").replace("MiB","").replace("KiB","").trim()
	                            );
	                            double downloaded = total * percentage / 100;

	                            currentProgress.setProgress(percentage);
	                            currentProgress.setTotalSize(sizeText);
	                            currentProgress.setDownloadedSize(String.format("%.2f %s", downloaded, unit));
	                            currentProgress.setSpeed(matcher.group(3).trim());
	                            currentProgress.setEta(matcher.group(4).trim());
	                        }
	                    } catch (Exception ignored) {}
	                }
	            }

	            int exitCode = currentProcess.waitFor();

	            if (isCancelled) {
	                // already handled above
	            } else if (isPaused) {
	                // user paused — leave file and progress as-is
	            } else if (exitCode == 0) {
	                currentProgress.setProgress(100);
	                currentProgress.setEta("00:00");
	                currentProgress.setSpeed("0 B/s");
	            } else {
	                deleteCurrentFile();
	                currentProgress.setEta("Failed");
	            }

	        } catch (Exception e) {
	            if (!isCancelled && !isPaused) {
	                System.err.println("Download error: " + e.getMessage());
	                deleteCurrentFile();
	                currentProgress.setEta("Failed");
	            }
	        }
	    });

	    // Java has a rule: it won't fully exit while any non-daemon thread is still running — it'll wait, potentially forever, for that thread to finish on its own. A daemon thread is the opposite — the JVM is allowed to kill it instantly and exit anyway, no questions asked, the moment every other normal thread is done.
	    downloadThread.setDaemon(true);
	    downloadThread.start();

	    DownloadResponseDto response = new DownloadResponseDto();
	    response.setFileName("Saved in download");
	    response.setTitle("Download started");
	    response.setPlatform("YouTube");
	    return response;
	}
	
	public DownloadResponseDto downloadVideo(String url, String quality) {
	    currentProgress = new DownloadProgressDto();
	    currentProgress.setProgress(0);
	    currentProgress.setDownloadedSize("0 MB");
	    currentProgress.setTotalSize("0 MB");
	    currentProgress.setSpeed("0 MB/s");
	    currentProgress.setEta("Calculating...");

	    String timestamp = String.valueOf(System.currentTimeMillis());
	    String outputTemplate = DOWNLOAD_DIR + "/%(title)s_" + timestamp + ".%(ext)s";
	    
	    String formatSelector = mapQualityToFormat(quality);   // ← convert "360p" → yt-dlp format
	    savedQuality = quality;                                 // ← needed for resume later

	    System.out.println("Quality param received: " + quality);       // ← add
	    System.out.println("Format selector resolved to: " + formatSelector);  // ← add
	    return startDownloadInternal(url, outputTemplate, formatSelector);
	}
	
	private String mapQualityToFormat(String quality) {
	    if (quality == null) return "bestvideo[height<=1080]+bestaudio/best[height<=1080]";

	    switch (quality.trim().toLowerCase()) {
	        case "360p":  return "bestvideo[height<=360]+bestaudio/best[height<=360]";
	        case "480p":  return "bestvideo[height<=480]+bestaudio/best[height<=480]";
	        case "720p":  return "bestvideo[height<=720]+bestaudio/best[height<=720]";
	        case "1080p": return "bestvideo[height<=1080]+bestaudio/best[height<=1080]";
	        default:      return "bestvideo[height<=1080]+bestaudio/best[height<=1080]";
	    }
	}
	
	// ── Shared delete helper ─────────────────────────────────────────────────
    private void deleteCurrentFile() {
        /*if (currentFilePath != null) {
            File file = new File(currentFilePath);
            if (file.exists()) {
                boolean deleted = file.delete();
                System.out.println(deleted
                    ? "Deleted partial file: " + currentFilePath
                    : "Could not delete file: " + currentFilePath
                );
            }
            currentFilePath = null;
        } else {*/
            // ── Fallback: scan Downloads for partial .part files ─────────────
            File dir = new File(DOWNLOAD_DIR);
            File[] partFiles = dir.listFiles((d, name) -> name.endsWith(".part"));
            if (partFiles != null) {
                for (File f : partFiles) {
                    System.out.println("Deleting .part file: " + f.getName());
                    f.delete();
                }
            }
        //}
    }
	
	public DownloadProgressDto  cancelDownload() {
		isCancelled = true;  // ← set flag first
		if(currentProcess != null && currentProcess.isAlive()) 
		{ 
			currentProcess.destroyForcibly(); 
		}
		currentProgress.setEta("Cancelled...");
		deleteCurrentFile();
		
		return currentProgress;
    }
	
	/*public DownloadProgressDto pauseDownload()
	{
		isPaused = true;
		if (currentProcess != null && currentProcess.isAlive()) {
	        currentProcess.destroyForcibly();*/
	        /*try {
	            currentProcess.waitFor(2, java.util.concurrent.TimeUnit.SECONDS); // ← wait for actual exit
	        } catch (InterruptedException ignored) {}*/
	    /*}
		currentProgress.setEta("Paused");
	    return currentProgress;
	}*/

	public DownloadProgressDto pauseDownload() 
	{
	    if (currentProcess == null || !currentProcess.isAlive()) {
	        // nothing running — completed, cancelled, or already exited; do nothing
	        return currentProgress;
	    }
	    isPaused = true;
	    currentProcess.destroyForcibly();
	    currentProgress.setEta("Paused");
	    return currentProgress;
	}
	
	public DownloadResponseDto resumeDownload() {
	    if (savedUrl == null || savedOutputTemplate == null) {
	        throw new IllegalStateException("Nothing to resume");
	    }
	    isPaused = false;
	    currentProgress.setEta("Calculating...");
	    String formatSelector = mapQualityToFormat(savedQuality); 
	    return startDownloadInternal(savedUrl, savedOutputTemplate, formatSelector); // refactor your thread-start logic into this
	}
	
	public DownloadProgressDto getProgress() {
        return currentProgress;
    }
}