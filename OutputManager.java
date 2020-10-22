
public class OutputManager {

	private DecisionMaker algorithm;
	private boolean isVerbose;
	private int numTrials;
	public OutputManager (String algorite, boolean isVerbose, int numTrials) 
	{
		algorithm = new DecisionMaker(algorite);
		this.isVerbose = isVerbose;
		this.numTrials = numTrials;
	}
	
	public void setupRandom(long randomseed, int numChannels)
	{
		algorithm.setup_random_normal(randomseed, numChannels);
	}
	
	public void setupCSV(String input)
	{
		algorithm.setup_file_normal(input);
	}
	
	public void setupRigged()
	{
		algorithm.setup_rig();
	}
	
	public String startSimulation()
	{
		StringBuilder sb = new StringBuilder("");
		sb.append("Algorithm used: " + algorithm.getAlgorithm() + "\n");
		if(isVerbose)
		{
			sb.append("\n-----------------------------Channel Probabilities------------------------------\n\n");
			sb.append(algorithm.listProbabilities() + "\n");
			sb.append("--------------------------------------------------------------------------------\n\n");
		}
		Object[] results = algorithm.run(numTrials);
		if(isVerbose)
		{
			sb.append("-----------------------------Result Statistics----------------------------------\n\n");
		}
		sb.append("Best Channel Found Was Channel " + results[0] + "\tEstimated Success Rate: " + results[1] +"\n");
		sb.append("Final Success Rate: " + results[2] + "\n");
		if(isVerbose) 
		{
			sb.append("\n--------------------------------------------------------------------------------\n\n");
		}
		
		return sb.toString();
	}
	

}
