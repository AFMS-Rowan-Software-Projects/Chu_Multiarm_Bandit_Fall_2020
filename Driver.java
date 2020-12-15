//UNCLASSIFIED

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;


// TEMPORARY DRIVER, ONLY USING FOR TESTING REWORKED CODE
public class Driver {
	
	private static ArgsManager argumentManager;
	
	public static void main(String[] args)
	{
		try
		{
			argumentManager = new ArgsManager(args);
			System.out.println(argumentManager.processArguments());
		}catch(InvalidArgumentException e)
		{
			System.out.println(e);
		}catch(IOException e)
		{
			System.out.println("There was a file exception. Make sure files supplied actually exist and try again.");
		}catch(Exception e)
		{
			System.err.println("Unexpected error encountered: here is the stack trace: ");
			e.printStackTrace();
		}
	}
}

