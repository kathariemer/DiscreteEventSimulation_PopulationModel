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
    public static final int IDX_TIME = 1, IDX_POP_F = 2, IDX_POP_M = 3, IDX_BIRTH_F = 4, IDX_BIRTH_M = 5;
    public static final int IDX_IMMI_F = 6, IDX_IMMI_M = 7, IDX_DEATH_F = 8, IDX_DEATH_M = 9, IDX_EMI_F = 10, IDX_EMI_M = 11;

    public ConfigurableSimulation(String inputFile) throws IOException {
        this.simulation = new Simulation(inputFile);
    }

    public int[] step() {
        int[] res = null;
        if (simulation.hasNext()) {
            res = simulation.next();
        }
        return res;
    }

    public int[] step(int k) {
        int[] res = null;
        for (int i = 0; i < k; i++) {
            if (simulation.hasNext()) {
                res = simulation.next();
            }
        }
        return res;
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
     * set parameter and reset simulation for a new run
     * @param birthRate changed birthrate
     */
    public void resetBirthrate(double birthRate) {
        if (simulation.setBirthrate(birthRate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetFemaleDeathRate(double rate) {
        if (simulation.setFemaleDeathRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetMaleDeathRate(double rate) {
        if (simulation.setMaleDeathRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetEmigrationRate(double rate) {
        if (simulation.setEmigrationRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }

    public void resetImmigrationRate(double rate) {
        if (simulation.setImmigrationRate(rate)) {
            simulation.reset();
        } else {
            System.err.println("Current simulation still runnable.");
        }
    }
}
