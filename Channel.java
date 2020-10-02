import java.util.Random;

public class Channel {


	private final double STAY_SUCCESS;
	private final double STAY_FAILURE;
	private Random random;
	private boolean currentState;
	private int successes = 0;
	public Channel(int randomseed) 
	{
		random = new Random(randomseed);
		random.nextDouble();
		STAY_SUCCESS = random.nextDouble();
		STAY_FAILURE = random.nextDouble();
		currentState = true;
	}

	public int getSuccesses() {
		return successes;
	}

	public double getSTAY_SUCCESS() {
		return STAY_SUCCESS;
	}

	public double getSTAY_FAILURE() {
		return STAY_FAILURE;
	}
	
	public boolean getState()
	{
		return currentState;
	}
	
	public boolean ping()
	{
		double chance = random.nextDouble();
		if(currentState)
		{
			if(chance < STAY_SUCCESS)
			{
				successes++;
			}
			else
			{
				currentState = false;
			}
		}
		else
		{
			if(chance > STAY_FAILURE)
			{
				currentState = true;
			}
		}
		
		return currentState;
	}

}
