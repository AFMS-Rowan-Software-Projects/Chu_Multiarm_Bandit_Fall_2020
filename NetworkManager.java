//UNCLASSIFIED

public interface NetworkManager {

	/**
	 * Pings the given channel, and returns whether the communication was successful.
	 * 
	 * @param index The index of the channel to ping.
	 * @return True if the communication was successful.
	 */
	public boolean pingChannel(int index);
	
	/**
	 * Returns the size of the network.
	 * 
	 * @return The size of the network.
	 */
	public int size();
	
}
