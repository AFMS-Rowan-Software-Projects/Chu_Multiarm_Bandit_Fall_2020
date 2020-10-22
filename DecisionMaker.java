public class DecisionMaker {

    Channel[] simulatedNetwork;

    public DecisionMaker(Channel[] simulatedNetwork){
        this.simulatedNetwork = simulatedNetwork;
    }

    public int bruteForce(int pingsPerChannel, int numTrials){

        int numSuccesses = 0;
		int numPings = 0;

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
        System.out.println("Brute Force Rate: " + numSuccesses/numPings);
        return bestChannel;
    }
    
}
