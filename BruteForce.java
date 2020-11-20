//UNCLASSIFIED

public class BruteForce extends DecisionMaker{

	private final int pingsPerChannel = 100;
	
	private int currChannel;
	private int currNumPings;
	private int currNumSuccess;
	private int bestChannel;
	private double bestRate;
	
	public BruteForce(NetworkManager network) {
		super(network);
		
		currChannel = 0;
		currNumPings = 0;
		currNumSuccess = 0;
		bestChannel = 0;
		bestRate = 0.0;
	}

	public boolean pingChannel()
	{
		boolean result = false;
		
		// Exploration phase
		if (currChannel < network.size())
		{
			// Pings the current channel, performs appropriate record keeping
			if (network.pingChannel(currChannel))
			{
				result = true;
				currNumSuccess++;
			}
			currNumPings++;
			
			// Switches to the next channel if we reach pingsPerChannel, performs appropriate record keeping
			if (currNumPings == pingsPerChannel)
			{
				double currRate = (double) currNumSuccess / pingsPerChannel;
				
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
			if (network.pingChannel(bestChannel))
			{
				result = true;
			}
		}
		return result;
	}
	
	public Object[] getResults(){
		Object[] results = {bestChannel, bestRate};
		return results;
	}
}
