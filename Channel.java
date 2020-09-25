import java.util.Random;

public class Channel {


	private final double STAY_SUCCESS;
	private final double SUCCESS_FAILURE;
	private Random random;
	private boolean currentState;
	private int successes = 0;
	public Channel(int randomseed) 
	{
		random = new Random(randomseed);
		random.nextDouble();
		STAY_SUCCESS = random.nextDouble();
		SUCCESS_FAILURE = random.nextDouble();
		currentState = true;
	}

	public int getSuccesses() {
		return successes;
	}

	public double getSTAY_SUCCESS() {
		return STAY_SUCCESS;
	}

	public double getSUCCESS_FAILURE() {
		return SUCCESS_FAILURE;
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
			if(chance > STAY_SUCCESS)
			{
				chance = random.nextDouble();
				if(chance < SUCCESS_FAILURE)
				{
					currentState = false;
				}
				else
				{
					successes ++;	
				}
			}
			else
			{
				successes ++;
			}
		}
		else
		{
			if(chance > 1 - STAY_SUCCESS)
			{
				chance = random.nextDouble();
				if(chance < 1 - SUCCESS_FAILURE)
				{
					currentState = true;
					successes ++;
				}
			}
		}
		
		return currentState;
	}

}
