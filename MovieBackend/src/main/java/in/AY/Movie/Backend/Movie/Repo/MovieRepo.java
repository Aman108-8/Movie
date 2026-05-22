package in.AY.Movie.Backend.Movie.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.AY.Movie.Backend.Movie.Entity.Movie;
import java.util.List;


public interface MovieRepo extends JpaRepository<Movie, Integer>
{
	List<Movie> findByTitleContainingIgnoreCase(String title);
}
