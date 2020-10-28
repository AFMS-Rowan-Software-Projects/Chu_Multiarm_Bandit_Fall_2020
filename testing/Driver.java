package testing;

import environment.*;
import algorithms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;


// TEMPORARY DRIVER, ONLY USING FOR TESTING REWORKED CODE
public class Driver {
	//Declare static variables to be used in markov chain algorithms
	private static OutputManager op;
	private static NetworkManager network;
	private static BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
	public static void main(String[] args) throws IOException
	{

		System.out.println("Enter the algorithm you wish to employ: ");
		String input = args[0].strip().toLowerCase();
		String algorithm = input;
		System.out.println(algorithm);

		System.out.println("Enter the number of trials to use said algorithm: ");
		int numTrials = Integer.parseInt(args[1].strip());
		System.out.println(numTrials);

		System.out.println("Would you like a verbose output?(T/F):");
		String decision = args[2].toUpperCase().strip();
		boolean isVerbose = (decision.equals("T"))?true:false;
		//op = new OutputManager(algorithm, isVerbose, numTrials);
		System.out.println(isVerbose);
		System.out.println("Enter setup type (Random/CSV/Rigged): ");
		String choice = args[3].strip().toLowerCase();
		System.out.println(choice);

		if(choice.equals("random"))
		{
			System.out.println("Specify the randomseed");
			long randomseed = Long.parseLong(reader.readLine().strip());
			System.out.println(randomseed);
			System.out.println("Specify the number of channels:");
			int numChannels = Integer.parseInt(reader.readLine().strip());
			System.out.println(numChannels);
			network = new SimulatedNetwork(numChannels, randomseed);
		}
		else if(choice.equals("csv"))
		{
			System.out.println("Specify the comma separated probabilities: ");
			String csp = reader.readLine().strip();
			System.out.println(csp);
			String[] probs = input.split(","); 

			double cprob[] = new double[probs.length];
			for (int i = 0; i < probs.length; i++)
			{
				cprob[i] = Double.parseDouble(probs[i]);
			}
			network = new SimulatedNetwork(cprob);
		}
		else
		{
			System.out.println("Rigging the channels.");
			network = new SimulatedNetwork();
		}

		if (input.equals("bruteforce")) {
			op = new OutputManager(network, new BruteForce(network), isVerbose, numTrials);
		}
		
		if (input.equals("upperconfidencebound")) {
			op = new OutputManager(network, new UpperConfidenceBound(network), isVerbose, numTrials);
		}
		
		String results = op.startSimulation();
		System.out.println(results);

	}
}