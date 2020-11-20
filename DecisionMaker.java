//UNCLASSIFIED

public abstract class DecisionMaker {

	protected NetworkManager network;
	
	public DecisionMaker(NetworkManager network)
	{
		this.network = network;
	}
	
	/**
	 * Chooses the next channel to ping, and returns whether it was successful.
	 * @return
	 */
	public abstract boolean pingChannel();
	
	/**
	 * Returns the fields of the DecisionMaker object.
	 * @return
	 */
	public abstract Object[] getResults();
}
