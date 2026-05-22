package in.AY.Movie.Backend.User.Implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.AY.Movie.Backend.Exception.ApiException;
import in.AY.Movie.Backend.Exception.ResourceNotFoundException;
import in.AY.Movie.Backend.User.Entity.User;
import in.AY.Movie.Backend.User.Payload.ApiResponse;
import in.AY.Movie.Backend.User.Payload.UserDto;
import in.AY.Movie.Backend.User.Repo.UserRepo;
import in.AY.Movie.Backend.User.Service.UserServices;

@Service
public class UserImpl implements UserServices{

	@Autowired
	private UserRepo ur;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.ur.findById(userId)
				.orElseThrow((()-> new ResourceNotFoundException("User", " Id ",userId)));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		
		User updateUser = this.ur.save(user);
		UserDto userDto1 = this.modelMapper.map(updateUser, UserDto.class);
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = ur.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User", " Id ", userId));
		
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {

	    List<User> users = ur.findAll();

	    List<UserDto> userDtos = users.stream()
	            .map(user -> this.modelMapper.map(user, UserDto.class))
	            .collect(Collectors.toList());
	    return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.ur.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User"," Id ", userId));
		
		this.ur.delete(user);
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		
		if(ur.existsByEmail(user.getEmail()))
		{
			throw new ApiException("Already email exist");
		}
		if(ur.existsByName(user.getName()))
		{
			throw new ApiException("Already Username exist please take another UserName");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User newUser = this.ur.save(user);
		return this.modelMapper.map(newUser, UserDto.class);
	}

	
}
