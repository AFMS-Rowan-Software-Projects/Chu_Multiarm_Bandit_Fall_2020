import java.io.BufferedReader;
import java.io.FileReader;
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
		if(findFlag("-h", args) != -1) 
		{
			System.out.println("\nUse: Driver.java -a algorithm number -t trial number -v Verbose output -s setup number");
			System.out.println("\nOther essential flags for random trial generation:");
			System.out.println("-rs: specifies the randomseed");
			System.out.println("-nc: specifies the number of channels");
			System.out.println("\nOther essential flags for csv trial generation:");
			System.out.println("-f: Specifies filename of CSV with cooresponding input.");
			System.out.print("\n-----------------------------Algorithms------------------------------\n");
			System.out.println("0: Brute Force");
			System.out.println("1: Upper Confidence Bound");
			System.out.println("---------------------------------------------------------------------\n");
			System.out.print("\n-----------------------------Setup------------------------------\n");
			System.out.println("0: Random");
			System.out.println("1: CSV");
			System.out.println("2: Rigged");
			System.out.println("----------------------------------------------------------------\n");
			
		}
		
		if(findFlag("-a", args) == -1)
		{
			System.out.println("You need the -a flag to specify the algorithm, use -h for help if needed.");
		}
		else if(findFlag("-t", args) == -1)
		{
			System.out.println("You need the -t flag to specify the number of trials run, use -h for help if needed.");
		}
		else if(findFlag("-s", args) == -1)
		{
			System.out.println("You need the -s flag to specify the input type. use -h for help if needed.");
		}
		else
		{
			int input = Integer.parseInt(args[findFlag("-a", args) + 1]);
			String algorithm = (input == 0)?"brute force":"upper confidence bound";
			System.out.println("Algorithm: " + algorithm);

			int numTrials = Integer.parseInt(args[findFlag("-t", args) + 1]);
			System.out.println("Trials: " + numTrials);

			boolean isVerbose = (findFlag("-v", args) == -1)?false:true;
			System.out.println("Verbose: " + isVerbose);
			
			int type = Integer.parseInt(args[findFlag("-s", args) + 1]);
			String choice = (type == 0)?"random":(type == 1)?"csv":"rigged";
			System.out.println("Input type: " + choice);

			boolean missingFlag = true;
			if(choice.equals("random"))
			{
				if(findFlag("-rs", args) == -1)
				{
					System.out.println("For random, -rs flag is needed. Refer to help with -h if needed.");
				}
				else if(findFlag("-nc", args) == -1)
				{
					System.out.println("For random, -nc flag is needed. Refer to help with -h if needed.");
				}
				else
				{
					missingFlag = false;
					long randomseed = Long.parseLong(args[findFlag("-rs", args) + 1]);
					System.out.println("Randomseed: " + randomseed);
					
					int numChannels = Integer.parseInt(args[findFlag("-nc", args) + 1]);
					System.out.println("Number of Channels: " + numChannels);
					network = new SimulatedNetwork(numChannels, randomseed);
				}
			}
			else if(choice.equals("csv"))
			{
				if(findFlag("-f", args) == -1)
				{
					System.out.println("For csv, -f flag is needed. Refer to help with -h if needed.");
				}
				else
				{
					try
					{
						reader = new BufferedReader (new FileReader (args[findFlag("-f", args) + 1]));
						String csp = reader.readLine();
						System.out.println("Comma separated probabilities: " + csp);
						String[] probs = csp.split(","); 

						double cprob[] = new double[probs.length];
						for (int i = 0; i < probs.length; i++)
						{
							cprob[i] = Double.parseDouble(probs[i]);
						}
						network = new SimulatedNetwork(cprob);
						
					}catch(Exception e)
					{
						System.out.println("Either file is corrupt, or the path specified is incorrect/doesn't exist. Try again.");
					}
					finally
					{
						reader.close();
					}
					missingFlag = false;
				}
			}
			else
			{
				missingFlag = false;
				System.out.println("Rigging the channels.");
				network = new SimulatedNetwork();
			}
			
			if(missingFlag)
			{
				System.out.println("Failed to make Output Manager due to missing subflags. Refer to help manual with -h if needed.");
			}
			else
			{
				if (algorithm.equals("brute force")) {
					op = new OutputManager(network, new BruteForce(network), isVerbose, numTrials);
				}
				
				if (algorithm.equals("upper confidence bound")) {
					op = new OutputManager(network, new UpperConfidenceBound(network), isVerbose, numTrials);
				}
				
				String results = op.startSimulation();
				System.out.println(results);
			}
		}
		
		
	}
	
	private static int findFlag(String flag, String[] input)
	{
		for(int i = 0; i < input.length; i++)
		{
			if(flag.equals(input[i])) return i;
		}
		
		return -1;
	}
}
