package in.AY.Movie.Backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.AY.Movie.Backend.User.Payload.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler 
{
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException res)
	{
		String message = res.getMessage();
		ApiResponse apiRes = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiRes, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> apiException(ApiException res)
	{
		String message = res.getMessage();
		ApiResponse apiRes = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiRes, HttpStatus.NOT_FOUND);
	}
}
