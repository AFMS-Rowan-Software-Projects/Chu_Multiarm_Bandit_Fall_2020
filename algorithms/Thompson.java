package algorithms;

import environment.*;


public class Thompson extends DecisionMaker {
       
    private int currChannel;
    private int currNumPings;
    private int bestChannel;
    private double bestRate;

    double[] cumuReward = new double[network.size()];
    double[] pingCounts = new double[network.size()];
    double[] successRate = new double[network.size()];

    public Thompson(NetworkManager network) {
        super(network);
        currChannel = 0;
        currNumPings = 0;
        bestChannel = 0;
        bestRate = 0.0;
    }

    @Override
    public boolean pingChannel() {
        boolean result = false;
        if(currNumPings < network.size())
        {
            currChannel = currNumPings;

            if (network.pingChannel(currChannel))
                cumuReward[currChannel] += 1.0;
            else
                cumuReward[currChannel] +=  0.0;

            pingCounts[currChannel]++;
        }
        else {
            double sample = 0;
            bestChannel = 0;
            double maxSample = -1;
            Channel[] net = ((SimulatedNetwork)network).getNetwork();
            for(int i = 0; i < net.length; i++) {
                sample = ((ThomChannel)net[i]).sample();
                if(sample > maxSample) {
                    maxSample = sample;
                    bestChannel = i;
                }
            }
            result = net[bestChannel].ping();
            currNumPings++;
            pingCounts[bestChannel]+=1.0;
            if(result) {
                cumuReward[bestChannel]+=1.0;
            }
        }
        return result;
    }
    
    static int maximumArgument(double[] vector)
    {
        double maxVal = vector[0];
        int result = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > maxVal) {
                maxVal = vector[i];
                result = i;
            }
        }
        return result;
    }
    
    @Override
    public Object[] getResults() {

        for(int i = 0; i < network.size(); i++) {
            if(pingCounts[i] != 0) {
                successRate[i] = (double) cumuReward[i] / pingCounts[i];
            }
        }

        bestRate = successRate[maximumArgument(successRate)];

        Object[] results = {bestChannel, bestRate};
        return results;
    }
}
