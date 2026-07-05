package in.AY.Movie.Backend.Movie.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Movie.Entity.Movie;
import in.AY.Movie.Backend.Movie.Payload.MovieDto;
import in.AY.Movie.Backend.Movie.Payload.MovieResponse;

public interface MovieService 
{
	MovieDto addMovie(MovieDto movie);//, String fileName);
	
	MovieDto UpdateMovie(UUID id, MovieDto movieDto);//, List<String> screenshotNames);
	
	void DeleteMovie(UUID id);
	
	MovieResponse getAllMovie(Integer pageSize, Integer pageNumber,String sortBy, String sortDir);
	
	MovieDto getMovieById(UUID id);
	
	List<MovieDto> searchMovie(String search);
}
