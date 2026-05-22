package in.AY.Movie.Backend.User.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.AY.Movie.Backend.User.Entity.User;



public interface UserRepo extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String email);
	Optional<User> findByName(String username);
	
	boolean existsByEmail(String email);
	
	boolean existsByName(String username);
}
