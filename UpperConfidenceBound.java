//UNCLASSIFIED

public class UpperConfidenceBound extends DecisionMaker{
	
	private int currChannel;
	private int currNumPings;
	private int bestChannel;
	private double bestRate;
		
	double[] cumuReward = new double[network.size()];
        double[] pingCounts = new double[network.size()];
        double[] avgReward = new double[network.size()];
        double[] decValues = new double[network.size()];
        double[] successRate = new double[network.size()];
    
	public UpperConfidenceBound(NetworkManager network) {
		super(network);
		
		currChannel = 0;
		currNumPings = 0;
		bestChannel = 0;
		bestRate = 0.0;
	}
	
	@Override
	public boolean pingChannel() {
		
		boolean result = false;
		
		//All networks must be pinged at least once or else the algorithm fails
		if(currNumPings < network.size())
		{
			currChannel = currNumPings;
			
		    if (network.pingChannel(currChannel))
		    	cumuReward[currChannel] += 1.0;
		    else
		    	cumuReward[currChannel] +=  0.0;
		    	
		    pingCounts[currChannel]++;    	
		}	
		else	//Once all channels are pinged once the algorithm can begin
		{
			for(int i = 0; i < network.size(); i++) {
	    		avgReward[i] = (cumuReward[i] * 1.0) / pingCounts[i];
	    	}
	    		
	    	for(int i = 0; i < network.size(); i++) {
	    		decValues[i] = avgReward[i] + Math.sqrt( (2.0 * Math.log(currNumPings) / pingCounts[i]) );
	    	}
	    	
	    	currChannel = maximumArgument(decValues);
	   	    	
	    	//If channel is successful, reward the channel
	    	if(network.pingChannel(currChannel)) {
	    		cumuReward[currChannel] += 1.0;
	    		result = true;
	    	}
	    	else {
	    		cumuReward[currChannel] += 0.0;
	    	}
		}
		
		pingCounts[currChannel]++;   	
		currNumPings++;
		bestChannel = maximumArgument(pingCounts);    	
    	
		return result;
	}
	
    static int maximumArgument(double[] vector)
    {
	double maxVal = vector[0];
	int result = 0;
	for (int i = 0; i < vector.length; i++) {
	    if (vector[i] > maxVal) {
		maxVal = vector[i];
		result = i;
	    }
	}
	return result;
    }

	@Override
	public Object[] getResults() {
		
		for(int i = 0; i < network.size(); i++) {
			successRate[i] = (double) cumuReward[i] / pingCounts[i];
    	}
		
		bestRate = successRate[maximumArgument(successRate)];
		
		Object[] results = {bestChannel, bestRate};
		return results;
	}
}
