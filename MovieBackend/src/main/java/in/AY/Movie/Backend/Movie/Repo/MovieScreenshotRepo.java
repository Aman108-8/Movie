package in.AY.Movie.Backend.Movie.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import java.util.List;


public interface MovieScreenshotRepo extends JpaRepository<MovieScreenShot, Integer>{
	List<MovieScreenShot> findByMovieId(Integer movieId);

    boolean existsByIdAndMovieId(Integer screenshotId, Integer movieId);
}
