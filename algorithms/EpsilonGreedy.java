package testing;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Math;

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

    private final int INITIALIZATION_FACTOR = 5;
    private final double EPSILON = 0.05;
    private final double NUMCHANCES = 10000;
    private final int SUBSETSIZE;

    protected NetworkManager network;

    private double[] assumedProbabilities;
    private int[] pingTracker;

    private PriorityQueue<Pair> subsetManager;

    public EpsilonGreedy(NetworkManager network){
        super(network);

        this.network = network;

        assumedProbabilities = new double[network.size()];
        pingTracker = new int[network.size()];

        SUBSETSIZE = (int) 0.2*network.size();

        subsetManager = new PriorityQueue<>();
        initializeProbabilities();
       
    }

    /**
     * First pass to initialize probabilities.
     */
    private void initializeProbabilities(){
        int size = network.size();
        for (int i = 0; i<size; i++){
            int successes = 0;
            for (int j = 0; j<INITIALIZATION_FACTOR; j++){
                if (network.pingChannel(i)){
                    successes++;
                }
            }
            assumedProbabilities[i] = (double) successes/INITIALIZATION_FACTOR;
            pingTracker[i] = INITIALIZATION_FACTOR;
            System.out.println(subsetManager.size());
            subsetManager.add(new Pair(i, assumedProbabilities[i]));
        }
    }

    /**
     * Exploitation/Exploration loop.
     */
    public boolean pingChannel(){
        for (int i = 0; i <= NUMCHANCES; i++){
            int randomChannel = (int) Math.random() * (network.size() + 1);  
            double greed = Math.random();
            if (greed< 1-EPSILON){//Exploration!
                boolean channelUpdater = network.pingChannel(randomChannel);
                assumedProbabilities[randomChannel] = ((assumedProbabilities[randomChannel]*pingTracker[randomChannel]) 
                                                        + (channelUpdater?1:0))/pingTracker[++randomChannel];
                subsetManager.add(new Pair(randomChannel, assumedProbabilities[randomChannel]));
            }
            //Else exploitation
        }
        return true;
    }

    public Object[] getResults(){
        /* THIS HERE IS FOR SUBSETS! -- USE A QUEUE WHEN THE TIME COMES
        Object[] results;// = new Object[SUBSETSIZE];
        
        Pair[] values = (Pair[]) subsetManager.toArray();
        for (int i = 0; i<=SUBSETSIZE; i++){
            results[i] = values[i].channel;
        }*/
        int bestChannel = subsetManager.peek().channel;
        Object[] results = {bestChannel, assumedProbabilities[bestChannel]};
        return results;
    }
}
