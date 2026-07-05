package in.AY.Movie.Backend.Movie.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.AY.Movie.Backend.Movie.Entity.Movie;
import java.util.List;
import java.util.UUID;


public interface MovieRepo extends JpaRepository<Movie, UUID>
{
	List<Movie> findByTitleContainingIgnoreCase(String title);
}
