package in.AY.Movie.Backend.Exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException
{
	String resourceName;
	String fieldName;
	UUID fieldValue;
	Long fieldVal;
	Integer value;
	String fieldData;
	
	public ResourceNotFoundException(String resourceName, String fieldName, UUID fieldValue){
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldValue=fieldValue;
		this.fieldName=fieldName;
	}
	
	public ResourceNotFoundException(String resourceName, String fieldName, Integer value){
		super(String.format("%s not found with %s : %s", resourceName, fieldName, value));
		this.resourceName = resourceName;
		this.value=value;
		this.fieldName=fieldName;
	}
	
	public ResourceNotFoundException(String resourceName, String fieldName, Long fieldVal){
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldVal));
		this.resourceName = resourceName;
		this.fieldVal=fieldVal;
		this.fieldName=fieldName;
	}
	
	public ResourceNotFoundException(String resourceName, String fieldName, String fieldData){
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldData));
		this.resourceName = resourceName;
		this.fieldData=fieldData;
		this.fieldName=fieldName;
	}
	
}
