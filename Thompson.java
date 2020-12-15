//UNCLASSIFIED

public class Thompson extends DecisionMaker {
       
    private int currChannel;
    private int currNumPings;
    private int bestChannel;
    private double bestRate;

    double[] cumuReward = new double[network.size()];
    double[] pingCounts = new double[network.size()];
    double[] successRate = new double[network.size()];
    double[] failures = new double[network.size()];

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
        double sample = 0;
        double maxSample = -1;
        if(currNumPings < network.size()){
            currChannel = currNumPings;

            if (network.pingChannel(currChannel)){
                cumuReward[currChannel] += 1.0;
            }
            else{
                failures[currChannel] +=  1.0;
            }

            pingCounts[currChannel]++;
            currNumPings++;
        }
        else {
            for(int i = 0; i < network.size(); i++) {
                sample = betaVariate(cumuReward[i],failures[i]);
                if(sample > maxSample) {
                    maxSample = sample;
                    bestChannel = i;
                }
            }
            result = network.pingChannel(bestChannel);
            currNumPings++;
            pingCounts[bestChannel]+=1.0;
            if(result) {
                cumuReward[bestChannel]+=1.0;
            }
            else{
                failures[bestChannel]+=1.0;
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
        bestChannel = maximumArgument(successRate);
        bestRate = successRate[bestChannel];

        Object[] results = {bestChannel, bestRate};
        return results;
    }
       
   private double gammaVariate(double alpha, double beta) {
        final double SG_MAGICCONST = 1.0 + Math.log(4.5);
        double x;
        if (alpha > 1.0) {
            double ainv = Math.sqrt(2.0 * alpha - 1.0);
            double bbb = alpha - Math.log(4.0);
            double ccc = alpha + ainv;

            while(true) {
                double u1 = Math.random();
                if (!((0.0000001 < u1) && (u1 < 0.9999999))) {
                    continue;
                }
                double u2 = 1.0 - Math.random();
                double v = Math.log(u1 / (1.0 - u1)) / ainv;
                x = alpha * Math.exp(v);
                double z = u1 * u1 * u2;
                double r = bbb + ccc * v - x;
                if ((r + SG_MAGICCONST - 4.5 * z >= 0.0) || (r >= Math.log(z))) {
                    return x * beta;
                }
            }
        }

        else if (alpha == 1.0) {
            return -1*Math.log(1.0 - Math.random()) * beta;
        }
        else {
            // successes is between 0 and 1 (exclusive)
            // Uses ALGORITHM GS of Statistical Computing - Kennedy & Gentle
            while(true) {
                double u = Math.random();
                double b = (Math.E + alpha) / Math.E;
                double p = b * u;
                if (p <= 1.0) {
                    x = Math.pow(p,1.0 / alpha);
                }
                else {
                    x = -1*Math.log((b - p) / alpha);
                }
                double u1 = Math.random();
                if (p > 1.0) {
                    if (u1 <= Math.pow(x,(alpha - 1.0))) {
                        break;
                    }
                }
                else if(u1 <= Math.exp(-x)) {
                    break;
                }
            }
            return x * beta;
        }
    }

    private double betaVariate(double successes, double failures) {
        double y = gammaVariate(successes, 1.0);
        return y / (y + gammaVariate(failures, 1.0));
    }
}
