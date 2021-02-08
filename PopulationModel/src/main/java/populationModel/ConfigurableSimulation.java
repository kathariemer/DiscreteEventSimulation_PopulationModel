package populationModel;

import populationModel.util.ImmigrationParameters;
import populationModel.util.PopulationParameters;

import java.io.IOException;

/**
 * A wrapper for simulation which e.g. may be used in a matlab environment. It allows to step through the tiemsteps and change parameters
 */
public class ConfigurableSimulation {

    private final Simulation simulation;
    /**
     * returns an array of variable names. The positions correspond to the return value of step()
     */
    public static final String[] HEADER = Simulation.HEADER.split(", ");

    /**
     * returns the Matlab index (starting at 1, not 0) of the time step in the returned array of step()
     */
    public static final int IDX_TIME = 1;

    /**
     * returns the Matlab index (starting at 1, not 0) of the updated female population count in the returned array of step()
     */
    public static final int IDX_POP_F = 2;

    /**
     * returns the Matlab index (starting at 1, not 0) of the updates male population count in the returned array of step()
     */
    public static final int IDX_POP_M = 3;

    /**
     * returns the Matlab index (starting at 1, not 0) of the newborn girls in the returned array of step()
     */
    public static final int IDX_BIRTH_F = 4;

    /**
     * returns the Matlab index (starting at 1, not 0) of the newborn boys in the returned array of step()
     */
    public static final int IDX_BIRTH_M = 5;

    /**
     * returns the Matlab index (starting at 1, not 0) of the male deaths in the returned array of step()
     */
    public static final int IDX_DEATH_F = 6;

    /**
     * returns the Matlab index (starting at 1, not 0) of the male deaths in the returned array of step()
     */
    public static final int IDX_DEATH_M = 7;

    /**
     * returns the Matlab index (starting at 1, not 0) of the immigration count in the returned array of step()
     */
    public static final int IDX_IMMI = 8;

    /**
     * returns the Matlab index (starting at 1, not 0) of the emigration count in the returned array of step()
     */
    public static final int IDX_EMI = 9;

    /**
     * generate a fresh simulation
     *
     * @param inputFile path to a file from which all initial parameters are read in
     * @throws IOException if the init file cannot be found
     */
    public ConfigurableSimulation(String inputFile) throws IOException {
        this.simulation = new Simulation(inputFile);
    }

    /**
     * perform one step of the simulation with current parameters
     * repeats cyclically (with t=1) when more than @code duration steps
     * have been performed
     *
     * @return array of population statistics. Semantics of the positions can be found with the static IDX_* methods
     */
    public int[] step() {
        if (!simulation.hasNext()) {
            simulation.reset();
        }
        return simulation.next();
    }

    /**
     * like step() but computes multiple steps at once and
     *
     * @param k number of steps
     * @return array of population statistics after k steps
     */
    public int[] step(int k) {
        if (k < 1) {
            throw new IllegalArgumentException("Please provide a positive step count.");
        }
        int[] res = null;
        for (int i = 0; i < k; i++) {
            if (!simulation.hasNext()) {
                simulation.reset();
            }
            res = simulation.next();
        }
        return res;
    }

    /**
     * Change the simulation's parameters using a vector of length 10 in the following order:
     * <ol>
     *     <li>muBirth</li>
     *     <li>slopeBirthRate</li>
     *     <li>muDeathF</li>
     *     <li>slopeDeathRateF</li>
     *     <li>muDeathM</li>
     *     <li>slopeDeathRateM</li>
     *     <li>lambdaImmi</li>
     *     <li>slopeImmiRate</li>
     *     <li>muEmi</li>
     *     <li>slopeEmiRate</li>
     * </ol>
     *
     * @param params a vector of length 10
     */
    public void resetAll(double[] params) {
        if (params.length != 10) {
            throw new IllegalArgumentException("Provide an array of exactly 10 parameters");
        }
        if (simulation.hasNext()) {
            System.err.println("Current simulation still runnable.");
        } else {
            PopulationParameters women = simulation.getWomenParams();
            PopulationParameters men = simulation.getMenParams();
            ImmigrationParameters immigration = simulation.getImmigrationParameters();

            women.setBirthRate(1/params[0]);
            women.setSlopeBirthRate(params[1]);

            women.setDeathRate(1 / params[2]);
            women.setSlopeDeathRate(params[3]);

            men.setDeathRate(1 / params[4]);
            men.setSlopeDeathRate(params[5]);

            immigration.setRate(params[6]);
            immigration.setSlope(params[7]);

            double emigrationRate = 1 / params[8];
            double emigrationSlope = params[9];
            women.setEmigrationRate(emigrationRate);
            men.setEmigrationRate(emigrationRate);
            women.setSlopeEmigrationRate(emigrationSlope);
            men.setSlopeEmigrationRate(emigrationSlope);

            simulation.reset();
        }
    }


    /**
     * Get the simulation's parameters (a vector of length 10) in the following order:
     * <ol>
     *     <li>muBirth</li>
     *     <li>slopeBirthRate</li>
     *     <li>muDeathF</li>
     *     <li>slopeDeathRateF</li>
     *     <li>muDeathM</li>
     *     <li>slopeDeathRateM</li>
     *     <li>lambdaImmi</li>
     *     <li>slopeImmiRate</li>
     *     <li>muEmi</li>
     *     <li>slopeEmiRate</li>
     * </ol>
     *
     * @return  a vector of length 10
     */
    public double[] getParams() {
        PopulationParameters women = simulation.getWomenParams();
        PopulationParameters men = simulation.getMenParams();
        ImmigrationParameters immigration = simulation.getImmigrationParameters();

        double[] params = new double[10];

        params[0] = 1/women.getBirthRate();
        params[1] = women.getSlopeBirthRate();

        params[2] = 1/women.getDeathRate();
        params[3] = women.getSlopeDeathRate();

        params[4] = 1/men.getDeathRate();
        params[5] = men.getSlopeDeathRate();

        params[6] = immigration.getRate();
        params[7] = immigration.getSlope();

        params[8] = 1/women.getEmigrationRate();
        params[9] = women.getSlopeEmigrationRate();

        return params;
    }

    /**
     * Change the initial population size
     * @param sizeF number of female inhabitants
     * @param sizeM number of male inhabitants
     */
    public void resetInitialPopulation(int sizeF, int sizeM) {
        if (simulation.hasNext()) {
            simulation.getWomenParams().setInitialPopulationSize(sizeF);
            simulation.getMenParams().setInitialPopulationSize(sizeM);
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    /**
     * set number of consecutive time steps the simulation models. Only possible at the end of a simulation.
     *
     * @param duration changed simulation duration
     */
    public void resetDuration(int duration) {
        if (simulation.setDuration(duration)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    /**
     * A simulation is mostly defined by all it's rates
     *
     * @return A very long string with all of the parameters rates (and slopes)
     */
    public String getCurrentSettings() {
        return simulation.toString();
    }

}
