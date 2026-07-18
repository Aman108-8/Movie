package in.AY.Movie.Backend.User.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.AY.Movie.Backend.Exception.ApiException;
import in.AY.Movie.Backend.User.Payload.JwtAuthRequest;
import in.AY.Movie.Backend.User.Payload.UserDto;
import in.AY.Movie.Backend.User.Security.JwtAuthResponse;
import in.AY.Movie.Backend.User.Security.JwtTokenHelper;
import in.AY.Movie.Backend.User.Service.UserServices;

@RestController
@RequestMapping("/auth/users")
public class AuthController {

	@Autowired
	private UserServices us;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenHelper jwtTokenHelper;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> CreateToken(@RequestBody JwtAuthRequest request)
	{
		authenticate(request.getEmail(), request.getPassword());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
		String token = jwtTokenHelper.generateToken(userDetails);
		JwtAuthResponse resp = new JwtAuthResponse();
		resp.setToken(token);
		return new ResponseEntity<JwtAuthResponse>(resp, HttpStatus.OK);
	}
	
	private void authenticate(String email, String password) 
	{
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
		try {
			this.authenticationManager.authenticate(token);
		}
		catch (BadCredentialsException e) 
		{
			throw new ApiException("login not succefully");
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
	{
		UserDto CreateUserDto = this.us.registerNewUser(userDto);
		return new ResponseEntity<>(CreateUserDto, HttpStatus.CREATED);
	}
}
