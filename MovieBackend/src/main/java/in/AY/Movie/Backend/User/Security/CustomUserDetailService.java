package in.AY.Movie.Backend.User.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.AY.Movie.Backend.Exception.ResourceNotFoundException;
import in.AY.Movie.Backend.User.Entity.User;
import in.AY.Movie.Backend.User.Repo.UserRepo;
import in.AY.Movie.Backend.User.Service.UserServices;

@Service
public class CustomUserDetailService implements UserDetailsService{

	
	@Autowired
	UserRepo ur;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User u = this.ur.findByEmail(email).orElseThrow(()->new 
				ResourceNotFoundException("Email", "Email", email));
		return u;
	}
	
}
