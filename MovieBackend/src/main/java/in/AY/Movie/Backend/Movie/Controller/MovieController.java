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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.config.AppConstants;
import in.AY.Movie.Backend.Movie.Payload.MovieDto;
import in.AY.Movie.Backend.Movie.Payload.MovieResponse;
import in.AY.Movie.Backend.Movie.Service.FileImageService;
import in.AY.Movie.Backend.Movie.Service.MovieService;
import in.AY.Movie.Backend.User.Payload.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/movie")
public class MovieController 
{
	@Autowired
	MovieService ms;
	
	@Autowired
	FileImageService fs;
	
	@Value("${project.movie.thumbnail}")
	private String thumbnailPath;
	
	@PostMapping(value="/")
	public ResponseEntity<MovieDto> addMovie(
	        @RequestBody MovieDto mov)
	{
		//String fileName= this.fs.UploadImage(thumbnailPath, image);
		MovieDto movieDto = ms.addMovie(mov);//, fileName);
		return new ResponseEntity<>(movieDto, HttpStatus.CREATED);
	}
	
	@PutMapping(value="/{movieId}")
	ResponseEntity<MovieDto> updateMovie(
			@RequestBody MovieDto mov,
	        @PathVariable("movieId") UUID id)  throws IOException
	{
		//String fileName = null;

		//thumbnail upload
	    /*if(image != null && !image.isEmpty())
	    {
	        fileName = this.fs.UploadImage(thumbnailPath, image);
	    }*/
	    
	    MovieDto movieDto = ms.UpdateMovie(id, mov);//, screenshotNames);
		return ResponseEntity.ok(movieDto);
	}
	
	@DeleteMapping("/{movieId}")
	ResponseEntity<ApiResponse> deleteMovie(@PathVariable("movieId") UUID id){
		ms.DeleteMovie(id);
		return new ResponseEntity<>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK );
	}
	
	@GetMapping("/")
	ResponseEntity<MovieResponse> getAllMovie(@RequestParam(value="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
	{
		MovieResponse movie = ms.getAllMovie(pageSize, pageNumber, sortBy, sortDir);
		return new ResponseEntity<MovieResponse>(movie, HttpStatus.OK);
	}
	
	@GetMapping("/{movieId}")
	ResponseEntity<MovieDto> getMovie(@PathVariable("movieId") UUID id)
	{
		return ResponseEntity.ok(this.ms.getMovieById(id));
	}
	
	@GetMapping("/search/{search}")
	public ResponseEntity<List<MovieDto>> searchMovieByTitle(@PathVariable("search") String search){
		List<MovieDto> result = ms.searchMovie(search);
		return new ResponseEntity<List<MovieDto>>(result, HttpStatus.OK);
	}
	
	@GetMapping(value = "/thumbnail/image/{imageName}")
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException{
		// create full path of image
	    String fullPath = thumbnailPath + File.separator + imageName;
	    // detect image type automatically
	    String contentType = Files.probeContentType(Paths.get(fullPath));
	    if(contentType == null){
	        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	    }
	    response.setContentType(contentType);
	    // read file
	    InputStream resource = this.fs.getResource(thumbnailPath, imageName);
	    // send file to browser
	    StreamUtils.copy(resource, response.getOutputStream());
	}
}
