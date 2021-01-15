package populationModel;

import java.io.IOException;

public class ConfigurableSimulation {
    private final Simulation simulation;

    public ConfigurableSimulation(String inputFile) throws IOException {
        this.simulation = new Simulation(inputFile);
    }

    public void run() {
        while (simulation.hasNext()) {
            simulation.next();
        }
    }

    /**
     * set parameter and reset simulation for a new run
     * @param birthRate changed birthrate
     */
    public void resetBirthrate(double birthRate) {
        if (simulation.setBirthrate(birthRate)) {
            simulation.reset();
        }
    }

    /**
     * set parameter and reset simulation for a new run
     * @param duration changed simulation duration
     */
    public void resetDuration(int duration) {
        if (simulation.setDuration(duration)) {
            simulation.reset();
        }
    }
}
