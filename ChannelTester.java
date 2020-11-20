//UNCLASSIFIED

import java.util.Random;
import java.util.Scanner;
public class ChannelTester {

    private static Scanner sc = new Scanner(System.in);
	private static Random rand;
    private Channel simulatedNetwork[];

    public ChannelTester(){
        
    }

    public Channel[] makeChannels(boolean randomGeneration){
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
        return simulatedNetwork;
    }

    public String displayNetworkInfo(){
        StringBuilder result = new StringBuilder("");
        result.append("Simulated Network: ");
		for (int i = 0; i < simulatedNetwork.length; i++)
		{
			result.append("Channel " + i + ":\tStay Success:  " + simulatedNetwork[i].getStaySuccess() + 
					"\tStay Failure:  " + simulatedNetwork[i].getStayFailure());
        }
        return result.toString();
    }
    
}
