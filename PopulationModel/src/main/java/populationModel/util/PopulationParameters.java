package populationModel.util;

public class PopulationParameters {
    private int initialPopulationSize;

    // values at time 0 - intercept
    private double deathRate;
    private double emigrationRate;
    private double birthRate;

    // slope coefficients
    private double slopeDeathRate;
    private double slopeEmigrationRate;
    private double slopeBirthRate;

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
    public double getDeathRate(int t) {
        double r = deathRate + t * slopeDeathRate;
        if (r < 0) {
            throw new IllegalArgumentException(String.format(
                    "Computed a death rate of %.2f. Only positive values are allowed.\n" +
                            "[t = %d, rate = %.2f, slope = %.2f]", r, t, deathRate, slopeDeathRate));
        }
        return r;
    }

    public double getEmigrationRate(int t) {
        double r = emigrationRate + t * slopeEmigrationRate;
        if (r < 0) {
            throw new IllegalArgumentException(String.format(
                    "Computed an emigration rate of %.2f. Only positive values are allowed.\n" +
                            "[t = %d, rate = %.2f, slope = %.2f]", r, t, emigrationRate, slopeEmigrationRate));
        }
        return r;
    }

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

    /**
     * birthrate at time t = 0
     *
     * @param newRate updated birthtate
     */
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
