package in.AY.Movie.Backend.Movie.Implementation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Exception.ResourceNotFoundException;
import in.AY.Movie.Backend.Movie.Entity.Movie;
import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import in.AY.Movie.Backend.Movie.Payload.MovieScreenShotDto;
import in.AY.Movie.Backend.Movie.Repo.MovieRepo;
import in.AY.Movie.Backend.Movie.Repo.MovieScreenshotRepo;
import in.AY.Movie.Backend.Movie.Service.FileImageService;
import in.AY.Movie.Backend.Movie.Service.MovieScreenshotService;

@Service
public class MovieScreenshotImpl implements MovieScreenshotService
{
	@Autowired
	MovieRepo mr;

	@Autowired
	ModelMapper mm;
	
	@Autowired
	FileImageService fs;
	
	@Autowired
	MovieScreenshotRepo movSS;
	
	@Value("${project.movie.screenshots}")
	private String screenshotsPath;
	
	@Value("${project.movie.thumbnail}")
	private String thumbnailPath;
	
	@Override
	public void addScreenshots(UUID movieId, List<MultipartFile> images) throws IOException {

	    Movie movie = mr.findById(movieId)
	            .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

	    for(MultipartFile file : images){

	        String fileName = fs.UploadImage(screenshotsPath, file);

	        MovieScreenShot ss = new MovieScreenShot();
	        ss.setImagePath(fileName);
	        ss.setMovie(movie);

	        movSS.save(ss);
	    }
	}
	
	@Override
	public String updateScreenshot(UUID movieId, Integer screenshotId, MultipartFile image) throws IOException {
		MovieScreenShot ss = movSS.findById(screenshotId)
            .orElseThrow(() -> new ResourceNotFoundException("Screenshot", "id", screenshotId));

		// Validate screenshot belongs to movie
		//if(ss.getMovie().getId().intValue() != movieId) 
		if (!ss.getMovie().getId().equals(movieId))
		{
			throw new ResourceNotFoundException(
			        "Screenshot",
			        "movieId",
			        movieId
			    );
	    }
		
	    String oldImage = ss.getImagePath();
	
	    File oldFile = new File(screenshotsPath + File.separator + oldImage);
	
	    if(oldFile.exists()){
	        oldFile.delete();
	    }
	
	    String newFile = fs.UploadImage(screenshotsPath, image);
	
	    ss.setImagePath(newFile);
	
	    movSS.save(ss);
	    
	    return newFile;
	}

	@Override
	public void deleteScreenShot(UUID movieId, Integer screenshotID) {
		MovieScreenShot ss = movSS.findById(screenshotID)
	            .orElseThrow(() -> new ResourceNotFoundException("Screenshot", "id", screenshotID));
		//if(ss.getMovie().getId().intValue() != movieId) 
		if (!ss.getMovie().getId().equals(movieId))
		{
			throw new ResourceNotFoundException(
			        "Screenshot",
			        "movieId",
			        movieId
			    );
	    }
		
	    String oldImage = ss.getImagePath();
	
	    File oldFile = new File(screenshotsPath + File.separator + oldImage);
	
	    if(oldFile.exists()){
	        oldFile.delete();
	    }
	    
	    movSS.delete(ss);
	}

	@Override
	public List<MovieScreenShotDto> getScreenShot(UUID movieId) {
		List<MovieScreenShot> screenshots = movSS.findByMovieId(movieId);

	    return screenshots.stream()
	            .map(ss -> mm.map(ss, MovieScreenShotDto.class))
	            .toList();
	}
	
	@Override
	public String updateThumbnail(UUID movieId, MultipartFile image) throws IOException {

	    Movie movie = mr.findById(movieId)
	            .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));

	    // 🔥 Delete old thumbnail
	    String oldImage = movie.getThumbnail();

	    if (oldImage != null) {
	    	File oldFile = new File(thumbnailPath + File.separator + oldImage);
	    	
	        if (oldFile.exists()) {
	            oldFile.delete();
	        }
	    }

	    String fileName = fs.UploadImage(thumbnailPath, image);

	    // 🔥 Update movie thumbnail
	    movie.setThumbnail(fileName);

	    // 🔥 Save updated movie
	    mr.save(movie);
		return fileName;
	}
}
