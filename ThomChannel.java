package environment;
import java.util.Random;

public class ThomChannel extends Channel {

    private int failures = 0;


    public ThomChannel(double staySuccess, double stayFailure)
    {
        super(staySuccess, stayFailure);
    }
    
    public int getFailures(){
        return failures;
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
                failures++;
            }
        }
        else
        {
            if(chance > stayFailure)
            {
                successes++;
                currentState = true;
            }
            else {
                failures++;
            }
        }

        return currentState;
    }

    public double sample() {
        return betaVariate();
    }

    private double gammaVariate(double successes, double failures) {
        final double SG_MAGICCONST = 1.0 + Math.log(4.5);
        double x;
        if (successes > 1.0) {
            double ainv = Math.sqrt(2.0 * successes - 1.0);
            double bbb = successes - Math.log(4.0);
            double ccc = successes + ainv;

            while(true) {
                double u1 = Math.random();
                if (!((0.0000001 < u1) && (u1 < 0.9999999))) {
                    continue;
                }
                double u2 = 1.0 - Math.random();
                double v = Math.log(u1 / (1.0 - u1)) / ainv;
                x = successes * Math.exp(v);
                double z = u1 * u1 * u2;
                double r = bbb + ccc * v - x;
                if ((r + SG_MAGICCONST - 4.5 * z >= 0.0) || (r >= Math.log(z))) {
                    return x * failures;
                }
            }
        }

        else if (successes == 1.0) {
            return Math.log(1.0 - Math.random()) * failures;
        }
        else {
            // successes is between 0 and 1 (exclusive)
            // Uses ALGORITHM GS of Statistical Computing - Kennedy & Gentle
            while(true) {
                double u = Math.random();
                double b = (Math.E + successes) / Math.E;
                double p = b * u;
                if (p <= 1.0) {
                    x = Math.pow(p,1.0 / successes);
                }
                else {
                    x = -1*Math.log((b - p) / successes);
                }
                double u1 = Math.random();
                if (p > 1.0) {
                    if (u1 <= Math.pow(x,(successes - 1.0))) {
                        break;
                    }
                }
                else if(u1 <= Math.exp(-x)) {
                    break;
                }
            }
            return x * failures;
        }
    }

    private double betaVariate() {
        double y = gammaVariate(successes, 1.0);
        return y / (y + gammaVariate(failures, 1.0));
    }
}
