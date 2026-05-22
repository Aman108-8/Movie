package in.AY.Movie.Backend.Movie.Service;

import java.util.List;

import in.AY.Movie.Backend.Movie.Payload.MovieQualityDto;

public interface MovieQualityService 
{
	MovieQualityDto addMovieQuality(Integer movieId, MovieQualityDto mq);
	
	MovieQualityDto updateQuality(Integer MovieQualityId, MovieQualityDto mq);
	
	void deleteQuality(Integer MovieQualityId);
	
	List<MovieQualityDto> getAllQuality(Integer MovieId);
	
	MovieQualityDto getQualityById(Integer qualityId);
}
