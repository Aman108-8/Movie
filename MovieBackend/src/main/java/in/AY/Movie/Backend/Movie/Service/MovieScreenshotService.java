package in.AY.Movie.Backend.Movie.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import in.AY.Movie.Backend.Movie.Payload.MovieScreenShotDto;

public interface MovieScreenshotService 
{
	String updateScreenshot(UUID movieId, Integer screenshotID, MultipartFile image) throws IOException;
	
	void addScreenshots(UUID movieId, List<MultipartFile> images) throws IOException;
	
	void deleteScreenShot(UUID movieId, Integer screenshotID);
	
	List<MovieScreenShotDto> getScreenShot(UUID movieId);
	
	public String updateThumbnail(UUID movieId, MultipartFile image) throws IOException;
}
