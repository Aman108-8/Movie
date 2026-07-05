package in.AY.Movie.Backend.Movie.Controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import in.AY.Movie.Backend.Movie.Payload.MovieScreenShotDto;
import in.AY.Movie.Backend.Movie.Service.FileImageService;
import in.AY.Movie.Backend.Movie.Service.MovieScreenshotService;
import in.AY.Movie.Backend.Movie.Service.MovieService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/movie")
public class MovieImageController 
{
	@Autowired
	MovieScreenshotService mScreenshot;
	
	@Autowired
	FileImageService fs;
	
	@Value("${project.movie.screenshots}")
	private String screenshotsPath;
	
	@Value("${project.movie.thumbnail}")
	private String thumbnailPath;
	
	@PostMapping("/{movieId}/screenshots")
	public ResponseEntity<String> uploadScreenshots(
	        @PathVariable UUID movieId,
	        @RequestParam("images") List<MultipartFile> images
	) throws IOException {

		mScreenshot.addScreenshots(movieId, images);

	    return ResponseEntity.ok("Screenshots uploaded");
	}
	
	@PutMapping("/{movieId}/screenshot/{screenshotId}")
	public ResponseEntity<String> updateScreenshot(
	        @PathVariable Integer screenshotId,
	        @PathVariable UUID movieId,
	        @RequestParam("image") MultipartFile image
	) throws IOException {

	    String fileName = mScreenshot.updateScreenshot(movieId, screenshotId, image);

	    return ResponseEntity.ok(fileName); // ✅ return new filename
	}
	
	/*@PutMapping("/{movieId}/thumbnail")
	public ResponseEntity<String> updateThumbnail(
	        @PathVariable Integer movieId,
	        @RequestParam("image") MultipartFile image
	) throws IOException {

	    mScreenshot.updateThumbnail(movieId, image); // create this method

	    return ResponseEntity.ok("Thumbnail Updated");
	}*/
	
	@PutMapping("/{movieId}/thumbnail")
	public ResponseEntity<String> updateThumbnail(
	        @PathVariable UUID movieId,
	        @RequestParam("image") MultipartFile image
	) throws IOException {

	    String fileName = mScreenshot.updateThumbnail(movieId, image);

	    return ResponseEntity.ok(fileName); // ✅ return actual filename
	}
	
	@DeleteMapping("/{movieId}/screenshot/{screenshotId}")
	public ResponseEntity<String> deleteScreenshot(
	        @PathVariable("screenshotId") Integer screenshotId,
	        @PathVariable("movieId") UUID movieId
	) throws IOException {

		mScreenshot.deleteScreenShot(movieId, screenshotId);

	    return ResponseEntity.ok("Screenshot Deleted");
	}
	
	@GetMapping("/{movieId}/screenshots")
	public ResponseEntity<List<MovieScreenShotDto>> getScreenshot(
	        @PathVariable("movieId") UUID movieId
	) throws IOException {

		List<MovieScreenShotDto> screenshots = mScreenshot.getScreenShot(movieId);

	    return ResponseEntity.ok(screenshots);
	}
	
	@GetMapping("/screenshot/image/{imageName}")
	public void downloadScreenshot(
	        @PathVariable String imageName,
	        HttpServletResponse response
	) throws IOException {

	    String fullPath = screenshotsPath + File.separator + imageName;

	    String contentType = Files.probeContentType(Paths.get(fullPath));
	    if (contentType == null) {
	        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	    }

	    response.setContentType(contentType);

	    InputStream resource = fs.getResource(screenshotsPath, imageName);
	    StreamUtils.copy(resource, response.getOutputStream());
	}
}
