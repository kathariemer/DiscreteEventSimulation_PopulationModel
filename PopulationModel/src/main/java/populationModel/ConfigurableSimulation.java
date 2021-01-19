package populationModel;

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
     * returns the Matlab indices (starting at 1, not 0) of the position of a value in the return of step()
     */
    public static final int IDX_TIME = 1,
            IDX_POP_F = 2,
            IDX_POP_M = 3,
            IDX_BIRTH_F = 4,
            IDX_BIRTH_M = 5,
            IDX_DEATH_F = 6,
            IDX_DEATH_M = 7,
            IDX_IMMI = 8,
            IDX_EMI = 9;

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
     *     <li>muBirth = [muBirth, slopeBirthrate]</li>
     *     <li>muDeathF</li>
     *     <li>muDeathM</li>
     *     <li>lambdaImmi</li>
     *     <li>muEmi</li>
     * </ol>
     *
     * @param params a vector
     */
    public void resetAll(double[] params) {
        if (params.length != 10) {
            throw new IllegalArgumentException("Provide an array of exactly 10 parameters");
        }
        if (simulation.setBirthrate(1 / params[0])) {
            simulation.setBirthSlope(params[1]);

            simulation.setFemaleDeathRate(1 / params[2]);
            simulation.setFemaleDeathSlope(params[3]);

            simulation.setMaleDeathRate(1 / params[4]);
            simulation.setMaleDeathSlope(params[5]);

            simulation.setImmigrationRate(params[6]);
            simulation.setImmigrationSlope(params[7]);

            simulation.setEmigrationRate(1 / params[8]);
            simulation.setEmigrationSlope(params[9]);
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    /**
     * Change the initial population size
     * @param sizeF number of female inhabitants
     * @param sizeM number of male inhabitants
     */
    public void resetInitialPopulation(int sizeF, int sizeM) {
        if (simulation.setFemaleInitialPopulationSize(sizeF)) {
            simulation.setMaleInitialPopulationSize(sizeM);
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
