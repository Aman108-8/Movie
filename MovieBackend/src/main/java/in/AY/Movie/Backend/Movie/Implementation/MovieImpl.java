package in.AY.Movie.Backend.Movie.Implementation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Exception.ResourceNotFoundException;
import in.AY.Movie.Backend.Movie.Entity.Movie;
import in.AY.Movie.Backend.Movie.Entity.MovieQuality;
import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import in.AY.Movie.Backend.Movie.Payload.MovieDto;
import in.AY.Movie.Backend.Movie.Payload.MovieQualityDto;
import in.AY.Movie.Backend.Movie.Payload.MovieResponse;
import in.AY.Movie.Backend.Movie.Payload.MovieScreenShotDto;
import in.AY.Movie.Backend.Movie.Repo.MovieRepo;
import in.AY.Movie.Backend.Movie.Repo.MovieScreenshotRepo;
import in.AY.Movie.Backend.Movie.Service.FileImageService;
import in.AY.Movie.Backend.Movie.Service.MovieService;

@Service
public class MovieImpl implements MovieService {

	@Autowired
	MovieRepo mr;

	@Autowired
	ModelMapper mm;
	
	@Autowired
	FileImageService fs;
	
	@Autowired
	MovieScreenshotRepo movSS;
	
	@Value("${project.movie.thumbnail}")
	private String thumbnailPath;
	
	@Value("${project.movie.screenshots}")
	private String screenshotsPath;

	@Override
	public MovieDto addMovie(MovieDto movieDto) {
		Movie movie = new Movie();

		movie.setTitle(movieDto.getTitle());
		movie.setDescription(movieDto.getDescription());
		movie.setGenre(movieDto.getGenre());
		movie.setRating(movieDto.getRating());
		movie.setReleaseYear(movieDto.getReleaseYear());

		/*if (fileName != null) {
			movie.setThumbnail(fileName);
		}*/

		// qualities
		/*if (movieDto.getQuality() != null) {
			for (MovieQualityDto qdto : movieDto.getQuality()) {
				MovieQuality q = new MovieQuality();
				q.setQuality(qdto.getQuality());
				q.setLink(qdto.getLink());

				movie.addQuality(q);
			}
		}*/
		Movie newMovie = mr.save(movie);
		return mm.map(newMovie, MovieDto.class);
	}

	@Override
	public MovieDto UpdateMovie(UUID id, MovieDto movieDto) 
	{
		Movie movie = mr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie", "Movie id", id));
		movie.setGenre(movieDto.getGenre());
		movie.setDescription(movieDto.getDescription());
		movie.setTitle(movieDto.getTitle());
		movie.setRating(movieDto.getRating());

		/*if (fileName != null) {
			String oldImage = movie.getThumbnail();
			
			if(oldImage != null) {
				File oldFile = new File(thumbnailPath + File.separator+oldImage);
				
				if (oldFile.exists()) {
	                oldFile.delete();
	            }
			}
			//movie.setThumbnail(fileName);
		}*/

		
		// Remove all elements from the list, but keep the same list object.

		/*if (movieDto.getQuality() != null) {
			movie.getQuality().clear();
			for (MovieQualityDto dto : movieDto.getQuality()) {

				MovieQuality q = new MovieQuality();
				q.setQuality(dto.getQuality());
				q.setLink(dto.getLink());
				q.setMovie(movie);

				movie.addQuality(q);
			}
		}*/

		Movie newMovie = mr.save(movie);
		return mm.map(newMovie, MovieDto.class);
	}

	@Override
	public void DeleteMovie(UUID id) {
		Movie movie = mr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie", "Movie id", id));
		mr.delete(movie);
	}

	@Override
	public MovieResponse getAllMovie(Integer pageSize, Integer pageNumber,String sortBy, String sortDir) 
	{
		Sort sort =(sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending());
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Movie> pageMovies = this.mr.findAll(p);
		List<Movie> allMovie = pageMovies.getContent();
		List<MovieDto> movieDto = allMovie.stream().map(movie->this.mm.map(movie, MovieDto.class)).collect(Collectors.toList());
		
		MovieResponse movieResp = new MovieResponse();
		
		movieResp.setContent(movieDto);
		movieResp.setPageNumber(pageMovies.getNumber());
		movieResp.setPageSize(pageMovies.getSize());
		movieResp.setTotalElement(pageMovies.getTotalElements());
		movieResp.setTotalPage(pageMovies.getTotalPages());
		movieResp.setLastPage(pageMovies.isLast());
		
		//List<Movie> movies = mr.findAll();
		//List<MovieDto> moviesDto = movies.stream().map(movie -> this.mm.map(movie, MovieDto.class))
			//	.collect(Collectors.toList());

		return movieResp;
	}

	@Override
	public MovieDto getMovieById(UUID id) {
		Movie movie = mr.findById(id).get();

		return mm.map(movie, MovieDto.class);
	}

	@Override
	public List<MovieDto> searchMovie(String search) {
		List<Movie> movies = mr.findByTitleContainingIgnoreCase(search);
		if (movies.isEmpty()) {
	        throw new ResourceNotFoundException("Movie", "title", search);
	    }

	    return movies.stream()
	            .map(movie -> mm.map(movie, MovieDto.class))
	            .toList();
	}
	

}
