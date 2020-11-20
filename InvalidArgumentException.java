//UNCLASSIFIED

public class InvalidArgumentException extends RuntimeException{

	private String info;
	
	public InvalidArgumentException(String message) 
	{
		info = message;
	}
	
	public String toString()
	{
		return info;
	}

}
