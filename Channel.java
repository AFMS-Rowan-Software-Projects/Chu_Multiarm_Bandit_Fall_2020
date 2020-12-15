//UNCLASSIFIED

import java.util.Random;

public class Channel {


	private double staySuccess;
	private double stayFailure;
	private boolean currentState;
	private int successes = 0;
	private Random random = new Random();
	public Channel(double staySuccess, double stayFailure) 
	{
		this.staySuccess = staySuccess;
		this.stayFailure = stayFailure;
		currentState = true;
	}

	public int getSuccesses() 
	{
		return successes;
	}

	public double getStaySuccess() 
	{
		return staySuccess;
	}

	public double getStayFailure() 
	{
		return stayFailure;
	}
	
	public void setStaySuccess(double staySuccess) 
	{
		this.staySuccess = staySuccess;
	}
	
	public void setStayFailure(double stayFailure) 
	{
		this.stayFailure = stayFailure;
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
			if(chance < staySuccess)
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
			if(chance > stayFailure)
			{
				successes++;
				currentState = true;
			}
		}
		
		return currentState;
	}

}
