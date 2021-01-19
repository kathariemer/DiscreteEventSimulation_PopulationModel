package populationModel;

import java.io.IOException;

/**
 * A wrapper for simulation which e.g. may be used in a matlab environment
 * It also allows to run a simulation, change a parameter, and run a new simulation with
 * th new parameters - therefore it was named ConfigurableSimulation
 */
public class ConfigurableSimulation {
    private final Simulation simulation;
    public static final String[] HEADER = Simulation.HEADER.split(", ");
    public static final int IDX_TIME = 1,
            IDX_POP_F = 2,
            IDX_POP_M = 3,
            IDX_BIRTH_F = 4,
            IDX_BIRTH_M = 5,
            IDX_DEATH_F = 6,
            IDX_DEATH_M = 7,
            IDX_IMMI = 8,
            IDX_EMI = 9;

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
     * @param params Ordered array of length 10
     */
    public void resetAll(double[] params) {
        if (params.length != 10) {
            throw new IllegalArgumentException("Provide an array of exactly 10 parameters");
        }
        throw new UnsupportedOperationException("If you want to use this method, " +
                "please send a text message to the programmer :D");
    }

    public void resetFemaleInitialPopulation(int size) {
        if (simulation.setFemaleInitialPopulationSize(size)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetMaleInitialPopulation(int size) {
        if (simulation.setMaleInitialPopulationSize(size)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    /**
     * set parameter and reset simulation for a new run
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
     * set expected number of births happening in 1 TimeUnit and
     * reset simulation for a new run
     *
     * @param birthRate changed birthrate, i.e. number of expected births per TimeUnit
     */
    public void resetLambdaBirth(double birthRate) {
        if (simulation.setBirthrate(birthRate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    /**
     * set the expected number of TimeUnits until 1 expected birth
     *
     * @param mu expected time between two births
     */
    public void resetMuBirth(double mu) {
        if (simulation.setBirthrate(1 / mu)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetLambdaFemaleDeath(double rate) {
        if (simulation.setFemaleDeathRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetMuFemaleDeath(double mu) {
        if (simulation.setFemaleDeathRate(1 / mu)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetLambdaMaleDeath(double rate) {
        if (simulation.setMaleDeathRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetMuMaleDeath(double mu) {
        if (simulation.setMaleDeathRate(1 / mu)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetLambdaEmigration(double rate) {
        if (simulation.setEmigrationRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetMuEmigration(double mu) {
        if (simulation.setEmigrationRate(1 / mu)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetLambdaImmigration(double rate) {
        if (simulation.setImmigrationRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetMuImmigration(double mu) {
        if (simulation.setImmigrationRate(1 / mu)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public String getCurrentSettings() {
        return simulation.toString();
    }

}
