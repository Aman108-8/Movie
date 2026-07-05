package in.AY.Movie.Backend.Download.Payload;

public class DownloadProgressDto {

	private double progress;
	private String totalSize;
	private String downloadedSize;
	private String speed;
	private String eta;
	//private String status;
    
	/*
	 * public String getStatus() { return status; } 
	 * public void setStatus(String status) { this.status = status; }
	 */
	public double getProgress() {
		return progress;
	}
	public void setProgress(double progress) {
		this.progress = progress;
	}
	public String getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}
	public String getDownloadedSize() {
		return downloadedSize;
	}
	public void setDownloadedSize(String downloadedSize) {
		this.downloadedSize = downloadedSize;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}

    // getters setters
    
    
}