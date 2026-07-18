package in.AY.Movie.Backend;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.AY.Movie.Backend.User.Entity.Role;
import in.AY.Movie.Backend.User.Repo.RoleRepo;


@SpringBootApplication
public class MovieBackendApplication implements CommandLineRunner 
{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(MovieBackendApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	//Basic Authentication
		@Override
		public void run(String... args) throws Exception {
			System.out.println(this.passwordEncoder.encode("xyz"));
			try {
				Role role = new Role();
				role.setId(1);
				role.setName("ROLE_ADMIN");
				
				Role role1 = new Role();
				role1.setId(2);
				role1.setName("ROLE_NORMAL");
				
				List<Role> roles = List.of(role,role1);
				
				List<Role> result = this.roleRepo.saveAll(roles);
				
				result.forEach(r->{
					System.out.println(r.getName());
				});
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
}
