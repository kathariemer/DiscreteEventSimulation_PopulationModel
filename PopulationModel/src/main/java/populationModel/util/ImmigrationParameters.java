package populationModel.util;

import static populationModel.util.RandomGenerator.randomNorm;

/**
 * object which bundles the parameters for immigration
 */
public class ImmigrationParameters {
    /**
     * initial immigration rate
     */
    private double initalRate;

    /**
     * slope of linear function which models change of immigration rate
     */
    private double slope;

    /**
     * proportion of female immigrants, number between [0, 1]
     */
    private final double proportionF;

    /**
     * obsolete, is 0
     */
    private final double meanAge;

    /**
     * obsolete, is 0
     */
    private final double sdAge;

    /**
     * initialize all parameters needed to compute the immigrations
     * @param initalRate initial immigration rate
     * @param slope growth of immigration rate
     * @param proportionF proportion in [0, 1] of female immigrants
     * @param meanAge obsolete, will always be 0
     * @param sdAge obsolete, will always be 0
     */
    public ImmigrationParameters(double initalRate, double slope, double proportionF, double meanAge, double sdAge) {
        this.initalRate = initalRate;
        this.slope = slope;
        this.proportionF = proportionF;
        this.meanAge = meanAge;
        this.sdAge = sdAge;
    }

    /**
     * normally distributed variable
     * @return the positive part of the generated integer
     */
    public int randomAge() {
        int age = (int) randomNorm(meanAge, sdAge);
        return (age > 0) ? age : 0;
    }

    /**
     * get immigration rate, which is a linear function of time
     * @param t time
     * @return current immigration rate
     */
    public double getImmigrationRate(int t) {
        return initalRate + t * slope;
    }

    /**
     * @return proportion of immigrant, which are women
     */
    public double getProportionF() {
        return proportionF;
    }

    // setters

    public void setRate(double rate) {
        this.initalRate = rate;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    @Override
    public String toString() {
        return "{" +
                "initalRate=" + initalRate +
                ", slope=" + slope +
                '}';
    }

    // getters

    public double getRate() {
        return initalRate;
    }

    public double getSlope() {
        return slope;
    }
}
