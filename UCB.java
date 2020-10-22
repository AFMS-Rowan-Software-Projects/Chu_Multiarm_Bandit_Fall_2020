public class UCB {

	public static void UpperConfidenceBound(Channel[] simulatedNetwork, int numTrials, int numChannels)
	{
		double[] cumuReward = new double[numChannels];
		double[] successCounts = new double[numChannels];
	    double[] avgReward = new double[numChannels];
	    double[] decValues = new double[numChannels];
	    
	    System.out.println("Each channel is pinged once intially");
	    
	    for(int i = 0; i < numChannels; i++) {
	    	if (simulatedNetwork[i].ping())
	    		cumuReward[i] += 1.0;
	    	else
	    		cumuReward[i] +=  0.0;
	    	
	    	successCounts[i]++;
	    }
	    
	    for(int j = 1; j <= numTrials; j++) {
	    	
	    	for(int i = 0; i < numChannels; i++) {
	    		avgReward[i] = (cumuReward[i] * 1.0) / successCounts[i];
	    	}
	    		
	    	for(int i = 0; i < numChannels; i++) {
	    		decValues[i] = avgReward[i] + Math.sqrt( (2.0 * Math.log(j) / successCounts[i]) );
	    	}
	    	
	    	int selected = maximumArgument(decValues);
	   	    	
	    	if(simulatedNetwork[selected].ping()) {
	    		cumuReward[selected] += 1.0;
	    	}
	    	else {
	    		cumuReward[selected] += 0.0;
	    		
	    	}
	    	
	    	successCounts[selected]++;
	    }
	    
	    int bestNetwork = maximumArgument(successCounts);
	    
	    System.out.println();
	      
	    for(int i = 0; i < numChannels; i++) {
    		System.out.println("Channel " + i + " was selected " + successCounts[i] + " times out of " + numTrials + " trials");
    	}
	    
	    System.out.println();
	    System.out.println("The best channel is channel " + bestNetwork);
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
}	
