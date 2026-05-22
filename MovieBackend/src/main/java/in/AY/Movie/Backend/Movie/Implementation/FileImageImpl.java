package in.AY.Movie.Backend.Movie.Implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.AY.Movie.Backend.Movie.Service.FileImageService;

@Service
public class FileImageImpl implements FileImageService
{

	@Override
	public String UploadImage(String path, MultipartFile file) throws IOException {
		
		/*
		 * if(path == null) { throw new RuntimeException("File is empty"); }
		 */
		
		String name = file.getOriginalFilename();
		
		if(name == null || !name.contains(".")) {
			throw new RuntimeException("Invalid file name");
		}
		
		String randomId = UUID.randomUUID().toString();
		String extension = name.substring(name.lastIndexOf("."));
		String fileName = randomId + extension;
		
		String filePath = path + File.separator+fileName;
		
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		return fileName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws IOException {
		String fullPath = path+File.separator+fileName;
		InputStream is = new FileInputStream(fullPath);
		return is;
	}

}
