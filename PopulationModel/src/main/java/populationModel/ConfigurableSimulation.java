package populationModel;

import java.io.IOException;

/**
 * A wrapper for simulation which e.g. may be used in a matlab environment
 * It also allows to run a simulation, change a parameter, and run a new simulation with
 * th new parameters - therefore it was named ConfigurableSimulation
 */
public class ConfigurableSimulation {
    private final Simulation simulation;
    public static final String HEADER = Simulation.HEADER;

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
}
