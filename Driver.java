import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

public class Driver {

	//Declare static variables to be used in markov chain algorithms
	private static OutputManager op;
	private static BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));

	public static void main(String[] args) throws IOException
	{
		System.out.println("Enter the algorithm you wish to employ: ");
		String input = args[0];
		String algorithm = input;
		System.out.println(algorithm);
		
		System.out.println("Enter the number of trials to use said algorithm: ");
		int numTrials = Integer.parseInt(args[1]);
		System.out.println(numTrials);
		
		System.out.println("Would you like a verbose output?(T/F):");
		String decision = args[2];
		boolean isVerbose = (decision.equals("T"))?true:false;
		op = new OutputManager(algorithm, isVerbose, numTrials);
		System.out.println(isVerbose);
		System.out.println("Enter setup type (Random/CSV/Rigged): ");
		String choice = args[3];
		System.out.println(choice);
		
		if(choice.equals("Random"))
		{
			System.out.println("Specify the randomseed");
			long randomseed = Long.parseLong(reader.readLine());
			System.out.println(randomseed);
			System.out.println("Specify the number of channels:");
			int numChannels = Integer.parseInt(reader.readLine());
			System.out.println(numChannels);
			op.setupRandom(randomseed, numChannels);
		}
		else if(choice.equals("CSV"))
		{
			System.out.println("Specify the comma separated probabilities: ");
			String csp = reader.readLine();
			System.out.println(csp);
			op.setupCSV(csp);
		}
		else
		{
			System.out.println("Rigging the channels.");
			op.setupRigged();
		}
		
		String results = op.startSimulation();
		System.out.println(results);
		
	}
}