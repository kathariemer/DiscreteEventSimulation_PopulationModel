import java.util.Random;

public class RandomGenerator {
    // todo singleton?

    private static final Random rand = new Random();

    /**
     * @param lambda inverse of rate
     * @return one draw from an exponential distribution
     */
    public double randomExp(double lambda) {
        //if (lambda > 1) System.err.println("Warning: exponential distribution with lambda > 1");
        return u2exp(randomUnif(), lambda);
    }

    /**
     * @return double from U(0, 1)
     */
    public double randomUnif() {
        return rand.nextDouble();
    }

    /**
     * @param mean expectation
     * @param sd standard deviation
     * @return one draw from normal distribution
     */
    public double randomNorm(double mean, double sd) {
        double z = rand.nextGaussian();
        return z*sd + mean;
    }

    public double u2exp(double u, double lambda) {
        return Math.log(1-u)/(-lambda);
    }
}
