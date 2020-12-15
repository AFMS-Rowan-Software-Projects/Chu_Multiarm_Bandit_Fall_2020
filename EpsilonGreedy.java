import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Math;
//import environment.NetworkManager;

/**
 * @author M Sarosh Khan
 * Generalized multi-arm bandit approach:
 * Probabilities modeled using two-state markov chains
 * Operates in linear time but requires high up-front
 * cost to produce actionable results.
 */
public class EpsilonGreedy extends DecisionMaker{

    public class Pair implements Comparable<Pair>{
        int channel;
        double probability;
        public Pair(int channel, double probability){
            this.channel = channel;
            this.probability = probability;
        }
        @Override
        public int compareTo(Pair c){
            if (this.probability>c.probability)
                return -1;
            else if (this.probability<c.probability)
                return 1;
            else   
                return 0;
        }
        
        public boolean equals(Pair e){
            return e.channel == this.channel;
        }
    }

    /**
     * @deprecated
     * Can be passed as an argument for PriorityQueue constructor.
     * Better off using compareTo in the user defined class.
     */
    
    public class probabilityComparator implements Comparator<Pair>{

        @Override
        public int compare(Pair x, Pair y){
            if (x.probability>y.probability)
                return -1;
            else if (x.probability<y.probability)
                return 1;
            else 
                return 0;
        }
    }
    
    
    private final double EPSILON = 0.05;
    //private final int SUBSETSIZE;

    protected NetworkManager network;

    private double[] assumedProbabilities;
    private int[] pingTracker;
    private int[] successes;
    
    private int currChannel;
    private int currTrial;

    private PriorityQueue<Pair> subsetManager;

    public EpsilonGreedy(NetworkManager network){
        super(network);
        this.network = network;

        assumedProbabilities = new double[network.size()];
        pingTracker = new int[network.size()];
        successes = new int[network.size()];

        //SUBSETSIZE = (int) 0.2*network.size();

        subsetManager = new PriorityQueue<>();
        this.currChannel = 0;
	this.currTrial = 0;
    }

    /**
     * Exploitation/Exploration loop.
     */
    public boolean pingChannel(){
        boolean success = false;
        if (currTrial < network.size()){
	    System.out.println(network.size());
	    success = network.pingChannel(currChannel);
            pingTracker[currChannel]++;
            successes[currChannel] += (success? 1:0);
            assumedProbabilities[currChannel] = (double) successes[currChannel]/pingTracker[currChannel];
            subsetManager.add(new Pair(currChannel, assumedProbabilities[currChannel]));
	    if(currTrial + 1 < network.size())
		currChannel++;
	    System.out.println(currChannel);
	    currTrial++;
        }
        else {
            double greed = Math.random();
            if (greed < 1 - EPSILON){
                currChannel = (int)(Math.random() * (network.size()));		
                subsetManager.remove(new Pair(currChannel, assumedProbabilities[currChannel]));
		success = network.pingChannel(currChannel);
                pingTracker[currChannel]++;
		successes[currChannel] += (success? 1:0);
                assumedProbabilities[currChannel] = (double) successes[currChannel]/pingTracker[currChannel];
                subsetManager.add(new Pair(currChannel, assumedProbabilities[currChannel]));
            }
            else {
                subsetManager.remove(new Pair(currChannel, assumedProbabilities[currChannel]));
		success = network.pingChannel(currChannel);
		pingTracker[currChannel]++;
		successes[currChannel] += (success? 1:0);
                assumedProbabilities[currChannel] = (double) successes[currChannel]/pingTracker[currChannel];
                subsetManager.add(new Pair(currChannel, assumedProbabilities[currChannel]));
            }                
        }
        return success;
    }

    public Object[] getResults(){
        /* THIS HERE IS FOR SUBSETS! -- USE A QUEUE WHEN THE TIME COMES
        Object[] results;// = new Object[SUBSETSIZE];
        
        Pair[] values = (Pair[]) subsetManager.toArray();
        for (int i = 0; i<=SUBSETSIZE; i++){
            results[i] = values[i].channel;
        }*/

        int bestChannel = maximumArgument(assumedProbabilities);
        Object[] results = {bestChannel, assumedProbabilities[bestChannel]};
        return results;
    }

        
    static int maximumArgument(double[] vector){
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
}
