package in.AY.Movie.Backend.User.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.AY.Movie.Backend.User.Entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{

}