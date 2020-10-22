package testing;

import environment.SimulatedNetwork;
import algorithms.*;

// TEMPORARY DRIVER, ONLY USING FOR TESTING REWORKED CODE
public class Driver {
	
	public static void main(String[] args)
	{
		SimulatedNetwork[] networkDictionary = new SimulatedNetwork[4];
		
		networkDictionary[0] = new SimulatedNetwork();		// Should make rigged 2-state network.
		System.out.println("Simulated Network 0");
		System.out.println(networkDictionary[0].toString());
		
		double[] networkState1 = {.5,.5, 0,1, 1,0, .1,.1};
		networkDictionary[1] = new SimulatedNetwork(networkState1);
		System.out.println("Simulated Network 1");
		System.out.println(networkDictionary[1].toString());
		
		networkDictionary[2] = new SimulatedNetwork(4, 541429);
		System.out.println("Simulated Network 2");
		System.out.println(networkDictionary[2].toString());
		
		networkDictionary[2] = new SimulatedNetwork(8, 742380);
		System.out.println("Simulated Network 3");
		System.out.println(networkDictionary[2].toString());
		
		DecisionMaker bruteForce = new BruteForce(networkDictionary[0]);
		
		int numSuccess = 0;
		for (int i = 0; i < 10000; i++)
		{
			if (bruteForce.pingChannel())
			{
				numSuccess++;
			}
		}
		
		System.out.println("Success rate: " + (double) numSuccess / 10000);
	}
}
