//UNCLASSIFIED

import java.util.Random;

public class SimulatedNetwork implements NetworkManager{

	private Channel[] network;
	
	/**
	 * Generates a new SimulatedNetwork with given number of channels, based on the given seed.
	 * 
	 * @param size The number of channels to generate.
	 * @param randomSeed The random seed to generate the channels with.
	 */
	public SimulatedNetwork(int size, long randomSeed)
	{
		Random rand = new Random(randomSeed);
		
		network = new Channel[size];
		
		for (int i = 0; i < size; i++)
		{
			network[i] = new Channel(rand.nextDouble(), rand.nextDouble());
		}
	}
	
	/**
	 * Generates a new SimulatedNetwork, where the stay success and stay failure rates of each channel are given.
	 * 
	 * @param channelInfo Holds the rates for each channel. 
	 */
	public SimulatedNetwork(double[] channelInfo)
	{
		
		network = new Channel[channelInfo.length / 2];
		for (int i = 0; i < channelInfo.length / 2; i++)
		{
			network[i] = new Channel(channelInfo[i * 2], channelInfo[i * 2 + 1]);
		}
	}
	
	/**
	 * Generates a rigged 2-state SimulatedNetwork.
	 */
	public SimulatedNetwork()
	{
		network = new Channel[2];
		
		network[0] = new Channel(0.95, 0.05);
		network[1] = new Channel(0.05, 0.95);
	}
	
	/**
	 * Pings the given channel, and returns whether the communication was successful.
	 * 
	 * @param index The index of the channel to ping.
	 * @return True if the communication was successful.
	 */
	public boolean pingChannel(int index)
	{
		return network[index].ping();
	}
	
	/**
	 * Returns the size of the network.
	 * 
	 * @return The size of the network.
	 */
	public int size()
	{
		return network.length;
	}
	
	/**
	 * Returns the network.
	 * 
	 * @return Channel array represting the network.
	 */
	public Channel[] getNetwork()
	{
		return network;
	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < network.length; i++)
		{
			result.append("Channel " + i + ":\tStay Success:  " + network[i].getStaySuccess() + 
					"\tStay Failure:  " + network[i].getStayFailure() + "\n");
        }
        return result.toString();
	}
	
}
