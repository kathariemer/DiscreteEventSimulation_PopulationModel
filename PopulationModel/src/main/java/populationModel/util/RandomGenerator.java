package populationModel.util;

import java.util.Random;

/**
 * Provides random generators (i.e. rexp, rnorm)
 */
public final class RandomGenerator {

    private static final Random rand = new Random();

    private RandomGenerator(){
    }

    /**
     * @param lambda inverse of rate
     * @return one draw from an exponential distribution
     */
    public static double randomExp(double lambda) {
        return u2exp(randomUnif(), lambda);
    }


    /**
     * @return double from U(0, 1)
     */
    public static double randomUnif() {
        return rand.nextDouble();
    }

    /**
     * @param mean expectation
     * @param sd standard deviation
     * @return one draw from normal distribution
     */
    public static double randomNorm(double mean, double sd) {
        double z = rand.nextGaussian();
        return z*sd + mean;
    }

    public static double u2exp(double u, double lambda) {
        return Math.log(1-u)/(-lambda);
    }
}
