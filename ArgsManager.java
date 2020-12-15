//UNCLASSIFIED

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ArgsManager {

	private String[] arguments; 
	private static OutputManager op;
	private static NetworkManager network;
	private static BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
	private StringBuilder sb = new StringBuilder("");
	public ArgsManager(String[] arguments) 
	{
		this.arguments = arguments;
		Object[] results = checkArgs(arguments);
		if(!(boolean)results[0]) throw new InvalidArgumentException(((StringBuilder)results[1]).toString());
	}
	
		
	public String processArguments() throws IOException
	{
		if(findFlag("-h", arguments) != -1) 
		{
			sb.append("\nUse: Driver.java -a algorithm number -t trial number -v Verbose output -s setup number");
			sb.append("\nOther essential flags for random trial generation:");
			sb.append("\n-rs: specifies the randomseed");
			sb.append("\n-nc: specifies the number of channels");
			sb.append("\nOther essential flags for csv trial generation:");
			sb.append("\n-f: Specifies filename of CSV with cooresponding input.");
			sb.append("\n-----------------------------Algorithms------------------------------\n");
			sb.append("\n0: Brute Force");
			sb.append("\n1: Upper Confidence Bound");
			sb.append("\n2: Epsilon Greedy");
			sb.append("\n3: Thompson Sampling");
			sb.append("\n---------------------------------------------------------------------\n");
			sb.append("\n-----------------------------Setup------------------------------\n");
			sb.append("\n0: Random");
			sb.append("\n1: CSV");
			sb.append("\n2: Rigged");
			sb.append("\n----------------------------------------------------------------\n");
			return sb.toString();
		}
		
		if(findFlag("-a", arguments) == -1)
		{
			throw new InvalidArgumentException("You need the -a flag to specify the algorithm, use -h for help if needed.");
		}
		else if(findFlag("-t", arguments) == -1)
		{
			throw new InvalidArgumentException("You need the -t flag to specify the number of trials run, use -h for help if needed.");
		}
		else if(findFlag("-s", arguments) == -1)
		{
			throw new InvalidArgumentException("You need the -s flag to specify the input type. use -h for help if needed.");
		}
		else
		{
			int input = Integer.parseInt(arguments[findFlag("-a", arguments) + 1]);
			String algorithm = (input == 0)?"brute force":(input == 1)?"upper confidence bound":
			    (input == 2)?"epsilon greedy":"thompson sampling";
			sb.append("\nAlgorithm: " + algorithm);

			int numTrials = Integer.parseInt(arguments[findFlag("-t", arguments) + 1]);
			sb.append("\nTrials: " + numTrials);

			boolean isVerbose = (findFlag("-v", arguments) == -1)?false:true;
			sb.append("\nVerbose: " + isVerbose);
			
			int type = Integer.parseInt(arguments[findFlag("-s", arguments) + 1]);
			String choice = (type == 0)?"random":(type == 1)?"csv":"rigged";
			sb.append("\nInput type: " + choice);

			boolean missingFlag = true;
			if(choice.equals("random"))
			{
				if(findFlag("-rs", arguments) == -1)
				{
					throw new InvalidArgumentException("For random, -rs flag is needed. Refer to help with -h if needed.");
				}
				else if(findFlag("-nc", arguments) == -1)
				{
					throw new InvalidArgumentException("For random, -nc flag is needed. Refer to help with -h if needed.");
				}
				else
				{
					missingFlag = false;
					long randomseed = Long.parseLong(arguments[findFlag("-rs", arguments) + 1]);
					sb.append("\nRandomseed: " + randomseed);
					
					int numChannels = Integer.parseInt(arguments[findFlag("-nc", arguments) + 1]);
					sb.append("\nNumber of Channels: " + numChannels);
					network = new SimulatedNetwork(numChannels, randomseed);
				}
			}
			else if(choice.equals("csv"))
			{
				if(findFlag("-f", arguments) == -1)
				{
					throw new InvalidArgumentException("For csv, -f flag is needed. Refer to help with -h if needed.");
				}
				else
				{
					reader = new BufferedReader (new FileReader (arguments[findFlag("-f", arguments) + 1]));
					String csp = reader.readLine();
					sb.append("\nComma separated probabilities: " + csp);
					String[] probs = csp.split(","); 

					double cprob[] = new double[probs.length];
					for (int i = 0; i < probs.length; i++)
					{
						cprob[i] = Double.parseDouble(probs[i]);
					}
					network = new SimulatedNetwork(cprob);
					
					missingFlag = false;
				}
			}
			else
			{
				missingFlag = false;
				sb.append("\nRigging the channels.");
				network = new SimulatedNetwork();
			}
			
			if(missingFlag)
			{
				throw new InvalidArgumentException("Failed to make Output Manager due to missing subflags. Refer to help manual with -h if needed.");
			}
			else
			{
				if (algorithm.equals("brute force")) {
				    op = new OutputManager(network, new BruteForce(network), isVerbose, numTrials);
				}
				
				if (algorithm.equals("upper confidence bound")) {
				    op = new OutputManager(network, new UpperConfidenceBound(network), isVerbose, numTrials);
				}
				if (algorithm.equals("epsilon greedy")){
				    op = new OutputManager(network, new EpsilonGreedy(network), isVerbose, numTrials); 
				}
				if (algorithm.equals("thompson sampling")){
				    op = new OutputManager(network, new Thompson(network), isVerbose, numTrials);
				}
				
				String results = op.startSimulation();
				sb.append("\n" + results);
			}
		}
		
		return sb.toString();
	}

	private static int findFlag(String flag, String[] input)
	{
		for(int i = 0; i < input.length; i++)
		{
			if(flag.equals(input[i])) return i;
		}
		
		return -1;
	}
	
	private static Object[] checkArgs(String[] input)
	{
		Object[] correctArgs = {true, new StringBuilder("")};
		
		boolean verboseFlag = (findFlag("-v", input) == -1)?false:true;
		if(!verboseFlag)
		{
			if(input.length == 1 && findFlag("-h", input) != -1)
			{
				//They need help, i
			}
			else if(input.length % 2 != 0)
			{
				correctArgs[0] = false;
				((StringBuilder) correctArgs[1]).append("\nAt least one flag is missing an argument. Check command again and use -h if you need help.");
			}
			else
			{
				for(int i = 0; i < input.length; i+=2)
				{
				   
				    if(input[i].equals("-f"))
					{
						//Ignore it
					}
					else
					{
						try
						{
							Integer.parseInt(input[i+1]);
						}catch(Exception e)
						{
							correctArgs[0] = false;
							((StringBuilder) correctArgs[1]).append("\n" + input[i] + " flag expects integer value. Refer to -h if needed.");
						}
					}
				}
			}
		}
		return correctArgs;
				
	}
	
}
