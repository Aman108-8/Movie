package in.AY.Movie.Backend.Download.Payload;

public class DownloadRequestDto {
	private String url;
	private String quality;

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
