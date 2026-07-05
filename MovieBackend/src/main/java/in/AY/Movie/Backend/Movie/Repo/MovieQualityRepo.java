package in.AY.Movie.Backend.Movie.Repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import in.AY.Movie.Backend.Movie.Entity.MovieQuality;

public interface MovieQualityRepo extends JpaRepository<MovieQuality, Long>
{	
	List<MovieQuality> findByMovie_Id(UUID movieId);
}
