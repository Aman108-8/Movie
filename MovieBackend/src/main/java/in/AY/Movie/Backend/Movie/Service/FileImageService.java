package in.AY.Movie.Backend.Movie.Service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileImageService 
{
	public String UploadImage(String path, MultipartFile file) throws IOException;
	
	public InputStream getResource(String path, String fileName) throws IOException;
}
