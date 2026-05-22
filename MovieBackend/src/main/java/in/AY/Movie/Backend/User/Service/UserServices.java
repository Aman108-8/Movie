package in.AY.Movie.Backend.User.Service;

import java.util.List;

import in.AY.Movie.Backend.User.Payload.UserDto;


public interface UserServices {
	
	UserDto registerNewUser(UserDto user);
	
	UserDto updateUser(UserDto user, Integer userId);
	
	UserDto getUserById(Integer userId);
	
	List<UserDto> getAllUsers();
	
	void deleteUser(Integer userId);
}
