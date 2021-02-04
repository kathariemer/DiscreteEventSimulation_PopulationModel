package populationModel.util;

/**
 * object which bundles the parameters for a population (either all female or all male)
 */
public class PopulationParameters {
    /**
     * population size at time 0
     */
    private int initialPopulationSize;

    // values at time 0 - intercept
    /**
     * initial death rate (at time 0)
     */
    private double deathRate;
    /**
     * initial emigration rate (at time 0)
     */
    private double emigrationRate;
    /**
     * initial birth rate (at time 0)
     */
    private double birthRate;

    // slope coefficients
    /**
     * slope for death rate, which is modeled as linear function in time
     */
    private double slopeDeathRate;
    /**
     * slope for emigration rate, which is modeled as linear function in time
     */
    private double slopeEmigrationRate;
    /**
     * slope for birth rate, which is modeled as linear function in time
     */
    private double slopeBirthRate;

    /**
     * encode, that the birthrate is undefined, used when modelling male populations
     */
    private final double UNDEFINED_BIRTHRATE = -1;

    /**
     * initialize simulation parameters for women
     *
     * @param initialPopulationSize number of people at time 0
     * @param deathRate             death rate for a person, value from interval (0, 1]
     * @param emigrationRate        emigration rate for a person
     * @param birthRate             birth rate for a person
     * @param slopeDeathRate        coefficient for death rate as (affine) linear function in t
     * @param slopeEmigrationRate   coefficient for emigration rate as (affine) linear function in t
     * @param slopeBirthRate        coefficient for birthrate as (affine) linear function in t
     */
    public PopulationParameters(int initialPopulationSize, double deathRate, double slopeDeathRate,
                                double emigrationRate, double slopeEmigrationRate,
                                double birthRate, double slopeBirthRate) {
        this.initialPopulationSize = initialPopulationSize;
        this.deathRate = deathRate;
        this.emigrationRate = emigrationRate;
        this.birthRate = birthRate;
        this.slopeDeathRate = slopeDeathRate;
        this.slopeEmigrationRate = slopeEmigrationRate;
        this.slopeBirthRate = slopeBirthRate;
    }

    /**
     * initialize simulation parameters for people, which cannot birth children
     *
     * @param initialPopulationSize number of people at time 0
     * @param deathRate             death rate for a person, value from interval (0, 1]
     * @param emigrationRate        emigration rate for a person
     * @param slopeDeathRate        coefficient for death rate as (affine) linear function in t
     * @param slopeEmigrationRate   coefficient for emigration rate as (affine) linear function in t
     */
    public PopulationParameters(int initialPopulationSize, double deathRate, double slopeDeathRate,
                                double emigrationRate, double slopeEmigrationRate) {
        this.initialPopulationSize = initialPopulationSize;
        this.deathRate = deathRate;
        this.emigrationRate = emigrationRate;
        this.slopeDeathRate = slopeDeathRate;
        this.slopeEmigrationRate = slopeEmigrationRate;
        birthRate = UNDEFINED_BIRTHRATE;
        slopeBirthRate = 0;
    }

    // getters

    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    // get dynamic values

    /**
     * @param t time
     * @return death rate for the population at time t; Throws IllegalArgumentException if for the chosen parameters, a negative rate is computed.
     */
    public double getDeathRate(int t) {
        double r = deathRate + t * slopeDeathRate;
        if (r < 0) {
            throw new IllegalArgumentException(String.format(
                    "Computed a death rate of %.2f. Only positive values are allowed.\n" +
                            "[t = %d, rate = %.2f, slope = %.2f]", r, t, deathRate, slopeDeathRate));
        }
        return r;
    }

    /**
     * @param t time
     * @return emigration rate for the population at time t; Throws IllegalArgumentException if for the chosen parameters, a negative rate is computed.
     */
    public double getEmigrationRate(int t) {
        double r = emigrationRate + t * slopeEmigrationRate;
        if (r < 0) {
            throw new IllegalArgumentException(String.format(
                    "Computed an emigration rate of %.2f. Only positive values are allowed.\n" +
                            "[t = %d, rate = %.2f, slope = %.2f]", r, t, emigrationRate, slopeEmigrationRate));
        }
        return r;
    }

    /**
     * get the birthrate at a given time
     * @param t time
     * @return a birthrate or an IllegalArgumentException, if for the chosen parameters the birthrate drops below 0 or above 1.
     */
    public double getBirthRate(int t) {
        if (birthRate == UNDEFINED_BIRTHRATE) {
            throw new NullPointerException("No birthrate was specified.");
        }
        double r = birthRate + t * slopeBirthRate;
        if (r < 0) {
            throw new IllegalArgumentException(String.format(
                    "Computed an birth rate of %.2f. Only positive values are allowed.\n" +
                            "[t = %d, rate = %.2f, slope = %.2f]", r, t, birthRate, slopeBirthRate));
        } else if (r >= 1) {
            throw new IllegalArgumentException(String.format(
                    "Computed an birth rate of %.2f. This is computationally too expensive.\n" +
                            "[t = %d, rate = %.2f, slope = %.2f]", r, t, birthRate, slopeBirthRate));
        }
        return r;
    }

    // setters

    public void setBirthRate(double newRate) {
        birthRate = newRate;
    }

    public void setDeathRate(double deathRate) {
        this.deathRate = deathRate;
    }

    public void setEmigrationRate(double emigrationRate) {
        this.emigrationRate = emigrationRate;
    }

    public void setSlopeDeathRate(double slopeDeathRate) {
        this.slopeDeathRate = slopeDeathRate;
    }

    public void setSlopeEmigrationRate(double slopeEmigrationRate) {
        this.slopeEmigrationRate = slopeEmigrationRate;
    }

    public void setSlopeBirthRate(double slopeBirthRate) {
        this.slopeBirthRate = slopeBirthRate;
    }

    public void setInitialPopulationSize(int initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
    }

    public double getDeathRate() {
        return deathRate;
    }

    public double getEmigrationRate() {
        return emigrationRate;
    }

    public double getBirthRate() {
        return birthRate;
    }

    public double getSlopeDeathRate() {
        return slopeDeathRate;
    }

    public double getSlopeEmigrationRate() {
        return slopeEmigrationRate;
    }

    public double getSlopeBirthRate() {
        return slopeBirthRate;
    }

    @Override
    public String toString() {
        if (birthRate == UNDEFINED_BIRTHRATE) {
            return "{" +
                    "initialPopulationSize=" + initialPopulationSize +
                    ", deathRate=" + deathRate +
                    ", slopeDeathRate=" + slopeDeathRate +
                    ", emigrationRate=" + emigrationRate +
                    ", slopeEmigrationRate=" + slopeEmigrationRate +
                    '}';
        } else {
            return "{" +
                    "initialPopulationSize=" + initialPopulationSize +
                    ", deathRate=" + deathRate +
                    ", slopeDeathRate=" + slopeDeathRate +
                    ", emigrationRate=" + emigrationRate +
                    ", slopeEmigrationRate=" + slopeEmigrationRate +
                    ", birthRate=" + birthRate +
                    ", slopeBirthRate=" + slopeBirthRate +
                    '}';
        }
    }
}
