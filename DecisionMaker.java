import java.util.Random;

public class DecisionMaker {

	private String algorithm;
	private Channel[] allChannels;
	public DecisionMaker(String algorite) 
	{
		algorithm = algorite;
	}
	
	
	public Object[] run(int numTrials)
	{
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
					if (currChannel < allChannels.length)
					{
						// Pings the current channel, performs appropriate record-keeping
						if (allChannels[currChannel].ping())
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
						if (allChannels[bestChannel].ping())
						{
							numSuccesses++;
						}
					}
					
					numPings++;
				}
		Object[] results = {bestChannel, bestRate, (double) numSuccesses / numPings};
		return results;
	}
	
	public void setup_rig()
	{
		allChannels = new Channel[2];
		allChannels[0] = new Channel(0.95, .05);
		allChannels[1] = new Channel(.05, .95);
	}
	
	public void setup_random_normal(long randomSeed, int numChannels)
	{
		Random rand = new Random(randomSeed);
		allChannels = new Channel[numChannels];
		for (int i = 0; i < numChannels; i++)
		{
			allChannels[i] = new Channel(rand.nextDouble(), rand.nextDouble());
		}
	}
	
	public void setup_file_normal(String input)
	{
		System.out.print("Enter Simulated Network String: ");
		String[] probabilities = input.split(","); 
		
		allChannels = new Channel[probabilities.length / 2];
		for (int i = 0; i < probabilities.length / 2; i++)
		{
			allChannels[i] = new Channel(Double.parseDouble(probabilities[i * 2]), Double.parseDouble(probabilities[i * 2 + 1]));
		}
	}
	
	public String listProbabilities()
	{
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < allChannels.length; i++)
		{
			sb.append("Channel " + i + ":\tStay Success:  " + allChannels[i].getStaySuccess()+ 
					"\tStay Failure:  " + allChannels[i].getStayFailure() + "\n");
		}
		return sb.toString();
	}
	
	public String getAlgorithm()
	{
		return algorithm;
	}
	
	

}
