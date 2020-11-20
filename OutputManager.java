//UNCLASSIFIED

public class OutputManager {

	private DecisionMaker algorithm;
	private boolean isVerbose;
	private int numTrials;
	private NetworkManager network;
	public OutputManager (NetworkManager network, DecisionMaker algorithm, boolean isVerbose, int numTrials) 
	{
		this.network = network;
		this.algorithm = algorithm;
		this.isVerbose = isVerbose;
		this.numTrials = numTrials;
	}
	
	
	public String startSimulation()
	{
		int numSuccess = 0;
		StringBuilder sb = new StringBuilder("");
		sb.append("Algorithm used: " + algorithm.getClass().getSimpleName() + "\n");
		if(isVerbose)
		{
			sb.append("\n-----------------------------Channel Probabilities------------------------------\n\n");
			sb.append(network.toString() + "\n");
			sb.append("--------------------------------------------------------------------------------\n\n");
		}
		for(int i = 0; i < numTrials; i++)
		{
			if(algorithm.pingChannel())
			{
				numSuccess++;
			}
		}
		Object[] results = algorithm.getResults();
		if(isVerbose)
		{
			sb.append("-----------------------------Result Statistics----------------------------------\n\n");
		}
		sb.append("Best Channel Found Was Channel " + results[0] + "\tFinal Success Rate: " + results[1] + "\n");
		if(isVerbose) 
		{
			sb.append("\n--------------------------------------------------------------------------------\n\n");
		}
		
		return sb.toString();
	}
	

}
