package in.AY.Movie.Backend.Movie.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.AY.Movie.Backend.Movie.Entity.MovieScreenShot;
import java.util.List;
import java.util.UUID;


public interface MovieScreenshotRepo extends JpaRepository<MovieScreenShot, Integer>{
	List<MovieScreenShot> findByMovieId(UUID movieId);

    boolean existsByIdAndMovieId(Integer screenshotId, UUID movieId);
}
