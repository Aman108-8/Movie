package in.AY.Movie.Backend.Movie.Service;

import java.util.List;

import java.util.UUID;
import in.AY.Movie.Backend.Movie.Payload.MovieQualityDto;

public interface MovieQualityService 
{
	MovieQualityDto addMovieQuality(UUID movieId, MovieQualityDto mq);
	
	MovieQualityDto updateQuality(Long MovieQualityId, MovieQualityDto mq);
	
	void deleteQuality(Long MovieQualityId);
	
	List<MovieQualityDto> getAllQuality(UUID MovieId);
	
	MovieQualityDto getQualityById(Long qualityId);
}
