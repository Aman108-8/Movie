package in.AY.Movie.Backend.Movie.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import in.AY.Movie.Backend.Movie.Payload.MovieScreenShotDto;

public interface MovieScreenshotService 
{
	String updateScreenshot(Integer movieId, Integer screenshotID, MultipartFile image) throws IOException;
	
	void addScreenshots(Integer movieId, List<MultipartFile> images) throws IOException;
	
	void deleteScreenShot(Integer movieId, Integer screenshotID);
	
	List<MovieScreenShotDto> getScreenShot(Integer movieId);
	
	public String updateThumbnail(Integer movieId, MultipartFile image) throws IOException;
}
