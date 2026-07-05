package in.AY.Movie.Backend.Movie.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.AY.Movie.Backend.Movie.Payload.MovieQualityDto;
import in.AY.Movie.Backend.Movie.Service.MovieQualityService;

@RestController
@RequestMapping("/api/movie/")
public class movieQualityController 
{
	@Autowired
	MovieQualityService mqs;
	
	@PostMapping("{movieId}/quality")
	ResponseEntity<MovieQualityDto> addQuality(@PathVariable("movieId") UUID movieId, @RequestBody MovieQualityDto qualityDto){
		MovieQualityDto mq = mqs.addMovieQuality(movieId, qualityDto);
		return ResponseEntity.ok(mq);
	}
	
	@PutMapping("quality/{qualityId}")
	ResponseEntity<MovieQualityDto> updateQuality(@PathVariable("qualityId") Long qualityId ,@RequestBody MovieQualityDto qualityDto){
		MovieQualityDto mq = mqs.updateQuality(qualityId, qualityDto);
		return ResponseEntity.ok(mq);
	}
	
	@DeleteMapping("quality/{qualityId}")
	ResponseEntity<String> deleteQuality(@PathVariable("qualityId") Long qualityId){
		mqs.deleteQuality(qualityId);
		return ResponseEntity.ok("Quality deleted");
	}
	
	@GetMapping("{movieId}/quality")
	ResponseEntity<List<MovieQualityDto>> getAllQuality(@PathVariable("movieId") UUID movieId){
		List<MovieQualityDto> mq = mqs.getAllQuality(movieId);
		return ResponseEntity.ok(mq);
	}
	
	@GetMapping("quality/{qualityId}")
	ResponseEntity<MovieQualityDto> getQualityById(@PathVariable("qualityId") Long qualityId){
		MovieQualityDto mq = mqs.getQualityById(qualityId);
		return ResponseEntity.ok(mq);
	}
}
