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
     * @param initialPopulationSize number of people at time 0
     * @param deathRate death rate for a person, value from interval (0, 1]
     * @param emigrationRate emigration rate for a person
     * @param birthRate birth rate for a person
     * @param slopeDeathRate coefficient for death rate as (affine) linear function in t
     * @param slopeEmigrationRate coefficient for emigration rate as (affine) linear function in t
     * @param slopeBirthRate coefficient for birthrate as (affine) linear function in t
     */
    public PopulationParameters(int initialPopulationSize,double deathRate, double slopeDeathRate,
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
     * @param initialPopulationSize number of people at time 0
     * @param deathRate death rate for a person, value from interval (0, 1]
     * @param emigrationRate emigration rate for a person
     * @param slopeDeathRate coefficient for death rate as (affine) linear function in t
     * @param slopeEmigrationRate coefficient for emigration rate as (affine) linear function in t
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
        return deathRate + t * slopeDeathRate;
    }

    public double getEmigrationRate(int t) {
        return emigrationRate + t * slopeEmigrationRate;
    }

    public double getBirthRate(int t) {
        return birthRate == UNDEFINED_BIRTHRATE ? -1 : birthRate + t * slopeBirthRate;
    }

    // setters

    /**
     * birthrate at time t = 0
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
}
