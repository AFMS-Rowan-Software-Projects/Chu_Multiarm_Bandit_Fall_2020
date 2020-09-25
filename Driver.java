import java.util.Random;
import java.util.Scanner;

public class Driver {

	//Declare static variables to be used in markov chain algorithms
	private static Scanner sc = new Scanner(System.in);
	private static Channel[] allChannels;
	private static boolean[] currentStates;
	private static int channelsRemaining;
	private static Random rand;
	private static int totalRuns = 0;
	
	public static void main(String[] args) 
	{
		//Prompt user for number of channels
		System.out.println("Enter the number of channels");
		int numChannels = sc.nextInt();
		//Keep all arrays this size
		allChannels = new Channel[numChannels];
		currentStates = new boolean[numChannels];
		//Get random seed for repeatability
		System.out.println("Enter the randomseed number");
		int randomseed = sc.nextInt();
		rand = new Random(randomseed);
		int iterator = 0;
		
		//Declare all channels with another randomseed randomly generated by this randomseed
		for(int i = 0; i < numChannels; i++)
		{
			allChannels[i] = new Channel(rand.nextInt(100));
			//Print their information
			System.out.println("Port number " + iterator + ": "
					+ 			"\nChance of staying in success: " + allChannels[i].getSTAY_SUCCESS() + 
								"\nChance of success to failure: " + allChannels[i].getSUCCESS_FAILURE());
			currentStates[iterator] = true;
			iterator++;
		}
		
		boolean isDone = false;
		channelsRemaining = 0;
		while(!isDone)
		{
			totalRuns++;
			for(int i = 0; i < allChannels.length; i++)
			{
				//If successful, add to growing tally of remaining channels we wish to analyze, else, keep it the same.
				if(currentStates[i])
				{
					currentStates[i] = allChannels[i].ping();
					if(currentStates[i]) channelsRemaining ++;
				}
			}
			
			if(channelsRemaining == 0)
			{
				isDone = true;
			}
			
		}
		
		int bestRateIndex = 0;
		int mostSuccessIndex = 0;
		double bestRate = 0;
		int mostSuccess = 0;
		
		for(int i = 0; i < allChannels.length; i++)
		{
			
			//Get the greatest Stay Success Rate from all channels
			double currRate = allChannels[i].getSTAY_SUCCESS();
			if( currRate > bestRate)
			{
				bestRate = currRate;
				bestRateIndex = i;
			}
			
			//Get the channel with the most successes
			int numSuccess = allChannels[i].getSuccesses();
			if(numSuccess > mostSuccess)
			{
				mostSuccess = numSuccess;
				mostSuccessIndex = i;
			}
		}
		
		System.out.println("Channel with bast STA_SUCCESS rate: Channel " + bestRateIndex + "\tRate: " + bestRate);
		System.out.println("Channel with most successes (we pick): Channel " + mostSuccessIndex + "\tSuccesses: " + mostSuccess + " out of " + totalRuns);
		
		
	}

}
