package populationModel.util;

import static populationModel.util.RandomGenerator.randomNorm;

public class ImmigrationParameters {
    private final double initalRate;
    private final double slope;

    private final double proportionF;
    private final double meanAge;
    private final double sdAge;

    public ImmigrationParameters(double initalRate, double slope, double proportionF, double meanAge, double sdAge) {
        this.initalRate = initalRate;
        this.slope = slope;
        this.proportionF = proportionF;
        this.meanAge = meanAge;
        this.sdAge = sdAge;
    }

    /**
     * noramlly distributed variable
     * @return the positive part of the generated integer
     */
    public int randomAge() {
        int age = (int) randomNorm(meanAge, sdAge);
        return (age > 0) ? age : 0;
    }

    public double getImmigrationRate(int t) {
        return initalRate + t * slope;
    }

    public double getProportionF() {
        return proportionF;
    }
}
