
public class BetaDistribution {

	public static double gammavariate(double alpha, double beta) {
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
	                if ((r + SG_MAGICCONST - 4.5 * z >= 0.0) || (r >= Math.log(z))){
	                	return x * beta;	
	                }
	            }
	        }

	        else if (alpha == 1.0) {
	        	return Math.log(1.0 - Math.random()) * beta;
	        	}
	        else {
	            // alpha is between 0 and 1 (exclusive)
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
	public static double betavariate(double alpha,double beta) {
	        double y = gammavariate(alpha, 1.0);
	        return y / (y + gammavariate(beta, 1.0));
	 }
}

