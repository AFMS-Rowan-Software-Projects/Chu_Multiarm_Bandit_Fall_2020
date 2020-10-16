import java.util.Random;
import java.util.Scanner;

public class Driver {

	//Declare static variables to be used in markov chain algorithms
	private static Scanner sc = new Scanner(System.in);
	private static Random rand;
	private static Channel[] simulatedNetwork;
	private static int numTrials;

	public static void main(String[] args)
	{
		/* 
		 * Generates Network Based off User Input
		 */
		
		System.out.print("Generate Channels Based On a Random Seed? (true/false): ");
		boolean randomGeneration = sc.nextBoolean();
		
		// Generates a network based on a random seed.
		if (randomGeneration)
		{
			System.out.print("Enter a seed for this trial: ");
			rand = new Random(sc.nextLong());
			
			//Prompt user for number of channels
			System.out.print("Enter the number of channels to generate: ");
			int numChannels = sc.nextInt();
			
			simulatedNetwork = new Channel[numChannels];
			
			for (int i = 0; i < numChannels; i++)
			{
				simulatedNetwork[i] = new Channel(rand.nextDouble(), rand.nextDouble());
			}
		}
		// Generates network as described by user.
		else
		{
			System.out.print("Use rigged 2 state environment? (true/false): ");
			boolean rigged = sc.nextBoolean();
			
			if (rigged)
			{
				simulatedNetwork = new Channel[2];
				simulatedNetwork[0] = new Channel(0.95, .05);
				simulatedNetwork[1] = new Channel(.05, .95);
			}
			else
			{
				/* Takes a String of the format described below, converting it to a simulated network.
				 * "StaySuccess1,StayFailure1,StaySuccess2,StayFailure2,...,StaySuccessN,StayFailureN"
				 * Ex: ".95,.05,.05,.95" 
				 *   Channel 1: .95 Success Rate from Success State		.05 Failure Rate from Failure State
				 *   Channel 2: .05 Success Rate from Success State		.95 Failure Rate from Failure State
				 */
				System.out.print("Enter Simulated Network String: ");
				String[] input = sc.next().split(","); 
				
				simulatedNetwork = new Channel[input.length / 2];
				for (int i = 0; i < input.length / 2; i++)
				{
					simulatedNetwork[i] = new Channel(Double.parseDouble(input[i * 2]), Double.parseDouble(input[i * 2 + 1]));
				}
			}
			
		}
		
		// Present only for debug, prints out network after it is generated.
		System.out.println("Simulated Network: ");
		for (int i = 0; i < simulatedNetwork.length; i++)
		{
			System.out.println("Channel " + i + ":\tStay Success:  " + simulatedNetwork[i].getStaySuccess() + 
					"\tStay Failure:  " + simulatedNetwork[i].getStayFailure());
		}
		
		// Sets number of channels to ping over the trial.
		System.out.print("Enter number of trials: ");
		numTrials = sc.nextInt();
		
		// Keeps track of overall success rate of each algorithm.
		int numSuccesses = 0;
		int numPings = 0;
		
		
		/* Naive / Brute Force Algorithm
		 * Communicates over each channel pingsPerChannel times, performs rest of pings on the channel with the highest 
		 * success rate.
		 */
		
		// Brute Force Algorithm Record Keeping
		int pingsPerChannel = 100;
		int currChannel = 0;
		int currNumPings = 0;
		int currNumSuccess = 0;
		int bestChannel = 0;
		double bestRate = 0.0;
		
		for (int i = 0; i < numTrials; i++)
		{
			// Exploration Phase
			if (currChannel < simulatedNetwork.length)
			{
				// Pings the current channel, performs appropriate record-keeping
				if (simulatedNetwork[currChannel].ping())
				{
					numSuccesses++;
					currNumSuccess++;
				}
				currNumPings++;
				
				// Switches to the next channel if we reach pingsPerChannel, performs appropriate record-keeping
				if (currNumPings == pingsPerChannel)
				{
					double currRate = (double) currNumSuccess / pingsPerChannel;
					
					// Debug Info
					System.out.println("Channel " + currChannel + "\tEstimated Success Rate: " + currRate);
					
					if (currRate > bestRate)
					{
						bestRate = currRate;
						bestChannel = currChannel;
					}
					
					currChannel++;
					currNumPings = 0; 
					currNumSuccess = 0;
				}
			}
			// Exploitation Phase
			else
			{
				if (simulatedNetwork[bestChannel].ping())
				{
					numSuccesses++;
				}
			}
			
			numPings++;
		}
		
		// Debug Info
		System.out.println("Best Channel Found Was Channel " + bestChannel + "\tEstimated Success Rate: " + bestRate);
		
		System.out.println("Final Success Rate: " + (double) numSuccesses / numPings);
		
	}

}
