package in.AY.Movie.Backend.Exception;

public class ResourceNotFoundException extends RuntimeException
{
	String resourceName;
	String fieldName;
	long fieldValue;
	String fieldData;
	
	public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue){
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldValue=fieldValue;
		this.fieldName=fieldName;
	}
	
	public ResourceNotFoundException(String resourceName, String fieldName, String fieldData){
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldData));
		this.resourceName = resourceName;
		this.fieldData=fieldData;
		this.fieldName=fieldName;
	}
	
}
