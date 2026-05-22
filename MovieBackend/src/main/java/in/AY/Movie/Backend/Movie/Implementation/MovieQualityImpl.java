package in.AY.Movie.Backend.Movie.Implementation;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.AY.Movie.Backend.Exception.ResourceNotFoundException;
import in.AY.Movie.Backend.Movie.Entity.Movie;
import in.AY.Movie.Backend.Movie.Entity.MovieQuality;
import in.AY.Movie.Backend.Movie.Payload.MovieQualityDto;
import in.AY.Movie.Backend.Movie.Repo.MovieQualityRepo;
import in.AY.Movie.Backend.Movie.Repo.MovieRepo;
import in.AY.Movie.Backend.Movie.Service.MovieQualityService;

@Service
public class MovieQualityImpl implements MovieQualityService
{
	@Autowired
	MovieQualityRepo mqr;
	
	@Autowired
	MovieRepo mr;
	
	@Autowired
	ModelMapper mm;
	
	@Override
	public MovieQualityDto addMovieQuality(Integer movieId, MovieQualityDto mq) {

	    MovieQuality movieQuality = mm.map(mq, MovieQuality.class);

	    Movie movie = mr.findById(movieId)
	            .orElseThrow(() -> new ResourceNotFoundException("Movie", "movie id", movieId));

	    movieQuality.setMovie(movie);

	    movie.getQuality().add(movieQuality);

	    MovieQuality savedQuality = mqr.save(movieQuality);

	    return mm.map(savedQuality, MovieQualityDto.class);
	}

	@Override
	public MovieQualityDto updateQuality(Integer movieQualityId, MovieQualityDto mq) {
		MovieQuality quality = mqr.findById(movieQualityId).orElseThrow(() -> new ResourceNotFoundException("Movie Quality", "Movie Quality id", movieQualityId));;
		quality.setLink(mq.getLink());
		quality.setQuality(mq.getQuality());
		
		return mm.map(mqr.save(quality), MovieQualityDto.class);
	}

	@Override
	public void deleteQuality(Integer movieQualityId) {
		MovieQuality quality = mqr.findById(movieQualityId).orElseThrow(() -> new ResourceNotFoundException("Movie Quality", "Movie Quality id", movieQualityId));
		mqr.delete(quality);
	}

	@Override
	public List<MovieQualityDto> getAllQuality(Integer movieId) {

	    List<MovieQuality> qualities = mqr.findByMovieId(movieId);

	    return qualities.stream()
	            .map(q -> mm.map(q, MovieQualityDto.class))
	            .toList();
	}

	@Override
	public MovieQualityDto getQualityById(Integer qualityId) {
		MovieQuality qualities = mqr.findById(qualityId).get();
		return mm.map(qualities, MovieQualityDto.class);
	}

}
