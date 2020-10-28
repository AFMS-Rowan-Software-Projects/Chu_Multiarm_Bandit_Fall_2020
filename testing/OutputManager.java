public class OutputManager {

	private DecisionMaker algorithm;
	private boolean isVerbose;
	private int numTrials;
	public OutputManager (DecisionMaker algorithm, boolean isVerbose, int numTrials) 
	{
		this.algorithm = algorithm;
		this.isVerbose = isVerbose;
		this.numTrials = numTrials;
	}
	
	
	public String startSimulation()
	{
		int numSuccess = 0;
    StringBuilder sb = new StringBuilder("");
		sb.append("Algorithm used: " + algorithm.getAlgorithm() + "\n");
		if(isVerbose)
		{
			sb.append("\n-----------------------------Channel Probabilities------------------------------\n\n");
			sb.append(algorithm.listProbabilities() + "\n");
			sb.append("--------------------------------------------------------------------------------\n\n");
		}
		for(int i = 0; i < numTrials; i++)
    {
      if(algorithm.pingChannel()){
          numSuccess++;
      }
    }
    Object[] results = algorithm.getResults();
		if(isVerbose)
		{
			sb.append("-----------------------------Result Statistics----------------------------------\n\n");
		}
		sb.append("Best Channel Found Was Channel " + results[0] + "\tEstimated Success Rate: " + results[1] +"\n");
		sb.append("Final Success Rate: " + (double)(numSuccess/numTrials) + "\n");
		if(isVerbose) 
		{
			sb.append("\n--------------------------------------------------------------------------------\n\n");
		}
		
		return sb.toString();
	}
	

}
