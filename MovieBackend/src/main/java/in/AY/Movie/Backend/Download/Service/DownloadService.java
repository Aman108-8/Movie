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
	private volatile String savedTimestamp = null;
	private volatile int    downloadPhase    = 1;   // 1 = video, 2 = audio
	private volatile double lastPercentage   = -1;  // tracks last seen % to detect reset
	private volatile double videoTotalSize  = 0;  // saved when phase 1 finishes
	private volatile String videoTotalUnit  = "";
	
	private DownloadResponseDto startDownloadInternal(String url, String outputTemplate, String formatSelector) {
	    isCancelled = false;
	    isPaused = false;
	    savedUrl = url;
	    savedOutputTemplate = outputTemplate;
	    downloadPhase  = 1;    // ← reset phase for fresh download
	    lastPercentage = -1;   // ← reset detector

	    Thread downloadThread = new Thread(() -> {
	        try {
	            ProcessBuilder pb = new ProcessBuilder(
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
	                currentProcess = pb.start();
	            }

	            BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));

	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (isCancelled) { deleteCurrentFile(); break; }
	                if (isPaused)    { break; }

	                System.out.println(line);

	                if (line.contains("Got error")
	                        || line.contains("Retrying")
	                        || line.contains("Unable to download webpage")
	                        || line.contains("getaddrinfo failed")
	                        || line.contains("Network is unreachable")
	                        || line.contains("Temporary failure in name resolution")) {
	                    currentProgress.setEta("Reconnecting...");
	                    continue;
	                }

	                if (line.contains("[download]") && line.contains("%")) {
	                    try {
	                        Pattern pattern = Pattern.compile(
	                            "(\\d+\\.?\\d*)%.*?of\\s+(.*?)\\s+at\\s+(.*?)\\s+ETA\\s+(.*)"
	                        );
	                        Matcher matcher = pattern.matcher(line);

	                        if (matcher.find()) 
	                        {
	                            double percentage = Double.parseDouble(matcher.group(1));
	                            String sizeText   = matcher.group(2).trim();

	                            // ── Phase detection ─────────────────────────────────
	                            // yt-dlp: video goes 0→100, then audio RESETS to 0→100
	                            // When we see percentage jump from >90 back to <10,
	                            // that means yt-dlp finished video and started audio.
	                            if (lastPercentage > 90 && percentage < 10) {
	                                downloadPhase = 2;
	                                System.out.println("Phase switched to AUDIO download");
	                            }
	                            lastPercentage = percentage;

	                            // ── Map to single combined progress bar ─────────────
	                            // Video:  0–85%  (video stream is the large file)
	                            // Audio: 85–100% (audio stream is small, ~3-5MB)
	                            double combinedProgress;
	                            if (downloadPhase == 1) {
	                                combinedProgress = percentage * 0.85;   // 0% → 85%
	                            } else {
	                                combinedProgress = 85 + (percentage * 0.15); // 85% → 100%
	                            }

	                            // ── Parse sizes for display ─────────────────────────
	                            String unit;
	                            if (sizeText.contains("GiB"))      unit = "GiB";
	                            else if (sizeText.contains("MiB")) unit = "MiB";
	                            else if (sizeText.contains("KiB")) unit = "KiB";
	                            else                                unit = "";

	                            double total = Double.parseDouble(
	                                sizeText.replace("GiB","").replace("MiB","").replace("KiB","").trim()
	                            );
	                            double downloaded = total * percentage / 100;

	                            // Update progress with COMBINED value, not raw yt-dlp %
	                            currentProgress.setProgress(Math.round((float) combinedProgress));
	                            currentProgress.setTotalSize(sizeText);
	                            currentProgress.setDownloadedSize(
	                                String.format("%.2f %s", downloaded, unit)
	                            );
	                            currentProgress.setSpeed(matcher.group(3).trim());

	                            // Show phase label in ETA area so user knows what's happening
	                            String etaValue = matcher.group(4).trim();
	                            if (downloadPhase == 2) {
	                                currentProgress.setEta(etaValue);
	                            } else {
	                                currentProgress.setEta(etaValue);
	                            }
	                        }
	                    } catch (Exception ignored) {}
	                }
	            }

	            int exitCode = currentProcess.waitFor();

	            if (isCancelled) {
	                // handled inside loop

	            } else if (isPaused) {
	                // leave file on disk for resume

	            } else if (exitCode == 0) {
	                // Both streams downloaded and ffmpeg merged — truly done
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

	    downloadThread.setDaemon(true);
	    downloadThread.start();

	    DownloadResponseDto response = new DownloadResponseDto();
	    response.setFileName("Saved in download");
	    response.setTitle("Download started");
	    response.setPlatform("YouTube");
	    return response;
	}
	
	public DownloadResponseDto downloadVideo(String url, String quality) 
	{
	    currentProgress = new DownloadProgressDto();
	    currentProgress.setProgress(0);
	    currentProgress.setDownloadedSize("0 MB");
	    currentProgress.setTotalSize("0 MB");
	    currentProgress.setSpeed("0 MB/s");
	    currentProgress.setEta("Calculating...");

	    String timestamp = String.valueOf(System.currentTimeMillis());
	    savedTimestamp = timestamp;   // ← save it for deleteCurrentFile()
	    
	    String outputTemplate = DOWNLOAD_DIR + "/%(title)s_" + timestamp + ".%(ext)s";
	    String formatSelector = mapQualityToFormat(quality);
	    savedQuality = quality;

	    System.out.println("Quality received: " + quality);
	    System.out.println("Format selector: " + formatSelector);

	    return startDownloadInternal(url, outputTemplate, formatSelector);
	}
	
	private String mapQualityToFormat(String quality) {
	    if (quality == null) return "bestvideo[height<=1080]+bestaudio/best[height<=1080]";

	    switch (quality.trim().toLowerCase()) {
	    	case "144p":  return "bestvideo[height<=144]+bestaudio/best[height<=144]";
	        case "360p":  return "bestvideo[height<=360]+bestaudio/best[height<=360]";
	        case "480p":  return "bestvideo[height<=480]+bestaudio/best[height<=480]";
	        case "720p":  return "bestvideo[height<=720]+bestaudio/best[height<=720]";
	        case "1080p": return "bestvideo[height<=1080]+bestaudio/best[height<=1080]";
	        default:      return "bestvideo[height<=1080]+bestaudio/best[height<=1080]";
	    }
	}
	
	// ── Shared delete helper ─────────────────────────────────────────────────
    /*private void deleteCurrentFile() {
        File dir = new File(DOWNLOAD_DIR);
        File[] partFiles = dir.listFiles((d, name) -> name.endsWith(".part"));
        if (partFiles != null) {
            for (File f : partFiles) {
                System.out.println("Deleting .part file: " + f.getName());
                f.delete();
            }
        }
    }*/
	
	private void deleteCurrentFile() {
	    File dir = new File(DOWNLOAD_DIR);
	    if (!dir.exists()) return;

	    File[] filesToDelete = dir.listFiles((d, name) -> {
	        // Delete any in-progress .part file
	        if (name.endsWith(".part")) return true;

	        // Delete yt-dlp's intermediate format files for THIS download
	        // e.g. title_1750336789123.f399.mp4  and  title_1750336789123.f251.webm
	        // These exist after the video stream finishes but BEFORE ffmpeg merges them.
	        // Without this, the video-only file stays on disk when you cancel mid-audio-download.
	        if (savedTimestamp != null && name.contains(savedTimestamp)) return true;

	        return false;
	    });

	    if (filesToDelete != null) {
	        for (File f : filesToDelete) {
	            boolean deleted = f.delete();
	            System.out.println(deleted
	                ? "Deleted: " + f.getName()
	                : "Could not delete: " + f.getName());
	        }
	    }
	} 
	
	public DownloadProgressDto cancelDownload() {
	    isCancelled = true;

	    synchronized (processLock) 
	    {
	        if (currentProcess != null && currentProcess.isAlive()) 
	        {
	            currentProcess.destroyForcibly();
	        }
	    }

	    // Only delete if download wasn't already complete.
	    // If progress is 100%, ffmpeg already merged and cleaned up —
	    // deleting now would remove the final finished file.
	    if (currentProgress.getProgress() < 100) {
	        deleteCurrentFile();
	    }

	    currentProgress.setEta("Cancelled");
	    return currentProgress;
	}
	
	public DownloadProgressDto pauseDownload() 
	{
		synchronized (processLock) {                          // ← add this
	        if (currentProcess == null || !currentProcess.isAlive()) {
	            return currentProgress;
	        }
	        isPaused = true;
	        currentProcess.destroyForcibly();
	    }
	    currentProgress.setEta("Paused");
	    return currentProgress;
	}
	
	public DownloadResponseDto resumeDownload() {
	    if (savedUrl == null || savedOutputTemplate == null) 
	    {
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