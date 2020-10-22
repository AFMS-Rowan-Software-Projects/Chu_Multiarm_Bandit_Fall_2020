
import java.util.Scanner;

public class Driver {

	private static Scanner sc = new Scanner(System.in);


	//Declare static variables to be used in markov chain algorithms
	private static ChannelTester tester; 
	private static DecisionMaker dm;
	private static Channel[] simulatedNetwork;
	public static void main(String[] args)
	{
		/* 
		 * Generates Network Based off User Input
		 */
		tester = new ChannelTester();
		System.out.print("Generate Channels Based On a Random Seed? (true/false): ");
		boolean randomGeneration = sc.nextBoolean();
		simulatedNetwork = tester.makeChannels(randomGeneration);

		dm = new DecisionMaker(simulatedNetwork);
		
		
		// Present only for debug, prints out network after it is generated.
		
		
		// Sets number of channels to ping over the trial.
		System.out.print("Enter number of trials: ");
		int numTrials = sc.nextInt();
		int bestChannel = dm.bruteForce(100, numTrials);

		System.out.println("Best Channel: " + bestChannel);

		
		
		/* Naive / Brute Force Algorithm
		 * Communicates over each channel pingsPerChannel times, performs rest of pings on the channel with the highest 
		 * success rate.
		 */
		
		// Brute Force Algorithm Record Keeping
		
		
	}

}
