package populationModel.util;

public class PopulationParameters {

    // values at time 0 - intercept
    private final double deathRate;
    private final double emigrationRate;
    private final double birthRate;

    // slope coefficients
    private final double slopeDeathRate;
    private final double slopeEmigrationRate;
    private final double slopeBirthRate;

    private final double UNDEFINED_BIRTHRATE = -1;

    /**
     * initialize simulation parameters for women
     * @param deathRate death rate for a person, value from interval (0, 1]
     * @param emigrationRate emigration rate for a person
     * @param birthRate birth rate for a person
     * @param slopeDeathRate coefficient for death rate as (affine) linear function in t
     * @param slopeEmigrationRate coefficient for emigration rate as (affine) linear function in t
     * @param slopeBirthRate coefficient for birthrate as (affine) linear function in t
     */
    public PopulationParameters(double deathRate, double slopeDeathRate,
                                double emigrationRate, double slopeEmigrationRate,
                                double birthRate, double slopeBirthRate) {
        this.deathRate = deathRate;
        this.emigrationRate = emigrationRate;
        this.birthRate = birthRate;
        this.slopeDeathRate = slopeDeathRate;
        this.slopeEmigrationRate = slopeEmigrationRate;
        this.slopeBirthRate = slopeBirthRate;
    }

    /**
     * initialize simulation parameters for people, which cannot birth children
     * @param deathRate death rate for a person, value from interval (0, 1]
     * @param emigrationRate emigration rate for a person
     * @param slopeDeathRate coefficient for death rate as (affine) linear function in t
     * @param slopeEmigrationRate coefficient for emigration rate as (affine) linear function in t
     */
    public PopulationParameters(double deathRate, double slopeDeathRate,
                                double emigrationRate, double slopeEmigrationRate) {
        this.deathRate = deathRate;
        this.emigrationRate = emigrationRate;
        this.slopeDeathRate = slopeDeathRate;
        this.slopeEmigrationRate = slopeEmigrationRate;
        birthRate = UNDEFINED_BIRTHRATE;
        slopeBirthRate = 0;
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
}
