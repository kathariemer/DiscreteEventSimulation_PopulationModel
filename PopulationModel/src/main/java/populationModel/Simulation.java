package populationModel;

import populationModel.person.Man;
import populationModel.person.Person;
import populationModel.person.Woman;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import static populationModel.util.RandomGenerator.*;

public class Simulation implements Iterator<String> {
    public static final String HEADER = String.format("time, populationF, populationM, %s", Event.HEADER);

    private int time;
    private final EventHistory eventList;

    // todo: unnecessary to save people, consider not saving or saving only some statistic
    private final HashMap<Integer, Woman> populationF = new HashMap<>();
    private final HashMap<Integer, Man> populationM = new HashMap<>();

    // TODO: change arrays to attributes -> alt+insert
    private final int initialPopulationSizeWomen;
    private final int initialPopulationSizeMen;

    private final SimulationParameters womenParams;
    private final SimulationParameters menParams;

    private final double immigrationRate, slopeImmigrationRate;

    private double cumulativeImmigrationTime;

    /**
     * read parameters from input file, where each line starts with the key name, followed by an equality symbol and
     * then the value. In order to ensure correct parsing, no punctuation is accepted.
     *
     * @param inputFile path to input file
     */
    public Simulation(String inputFile) throws IOException, NumberFormatException {
        // create and load default properties
        Properties properties = new Properties();
        FileInputStream inputStream= new FileInputStream(inputFile);
        properties.load(inputStream);
        inputStream.close();

        // init parameters
        initialPopulationSizeWomen = Integer.parseInt(properties.getProperty("init_f"));
        initialPopulationSizeMen = Integer.parseInt(properties.getProperty("init_m"));

        immigrationRate = 1 / Double.parseDouble(properties.getProperty("inv_immRate"));
        slopeImmigrationRate = Double.parseDouble(properties.getProperty("slope_immRate"));

        double deathRateWomen = 1 / Double.parseDouble(properties.getProperty("inv_deathRate_f"));
        double slopeDeathRateWomen = Double.parseDouble(properties.getProperty("slope_deathRate_f"));
        double emigrationRateWomen = 1 / Double.parseDouble(properties.getProperty("inv_emRate_f"));
        double slopeEmigrationRateWomen = Double.parseDouble(properties.getProperty("slope_emRate_f"));
        double birthRate = 1 / Double.parseDouble(properties.getProperty("inv_birthRate"));
        double slopeBirthRate = Double.parseDouble(properties.getProperty("slope_birthRate"));

        womenParams = new SimulationParameters(deathRateWomen, slopeDeathRateWomen,
                emigrationRateWomen, slopeEmigrationRateWomen,
                birthRate, slopeBirthRate);

        double deathRateMen = 1 / Double.parseDouble(properties.getProperty("inv_deathRate_m"));
        double slopeDeathRateMen = Double.parseDouble(properties.getProperty("slope_deathRate_m"));
        double emigrationRateMen = 1 / Double.parseDouble(properties.getProperty("inv_emRate_m"));
        double slopeEmigrationRateMen = Double.parseDouble(properties.getProperty("slope_emRate_m"));

        menParams = new SimulationParameters(deathRateMen, slopeDeathRateMen,
                emigrationRateMen, slopeEmigrationRateMen);

        int duration = Integer.parseInt(properties.getProperty("timeSteps"));
        eventList = new EventHistory(duration);
        initPopulation(0);
    }


    /**
     * Initialize a population of x women and y men
     *
     * @param time0
     */
    private void initPopulation(int time0) throws IllegalArgumentException {
        cumulativeImmigrationTime = (float) time0;
        if (time0 != 0) {
            throw new IllegalArgumentException("please start your simulation at time 0.");
        }
        // todo: maybe change people's ages
        for (int i = 0; i < initialPopulationSizeWomen; i++) {
            Woman w = new Woman(time0, womenParams);
            addPerson(w);
        }

        for (int i = 0; i < initialPopulationSizeMen; i++) {
            Man m = new Man(time0, menParams);
            addPerson(m);
        }
    }


    /**
     * add newly birthed person to female or male population, depending on gender
     * and schedule associated life events
     *
     * @param w populationModel.person.Person
     */
    private void addPerson(Woman w) {
        eventList.addEvents(w);
        populationF.put(w.getID(), w);
    }

    private void addPerson(Man m) {
        eventList.addEvents(m);
        populationM.put(m.getID(), m);
    }

    @Override
    public boolean hasNext() {
        return time < eventList.getDuration();
    }


    /**
     * compute a time step and advance attribute "time" by 1
     *
     * @return simulation statistics representing in a comma separated format
     */
    @Override
    public String next() {
        Event e = eventList.getEvent(time);
        if (e != null) {
            // birth
            for (int i = 0; i < e.getBirths(); i++) {
                if (randomUnif() < 0.5) {
                    Woman w = new Woman(time, womenParams);
                    addPerson(w);
                } else {
                    Man m = new Man(time, menParams);
                    addPerson(m);
                }
            }

            // immigrations
            addImmigrations(time, e);
            for (int j = 0; j < e.getImmigrationsFemale(); j++) {
                // TODO: add "age"? i.e. assume that person has age ~ N(30, 10)???
                int birthYear = time - (int) randomNorm(30, 10);
                Woman w = new Woman(birthYear, womenParams);
                addPerson(w);
            }
            for (int i = 0; i < e.getImmigrationsMale(); i++) {
                int birthYear = time - (int) randomNorm(30, 10);
                Man m = new Man(birthYear, menParams);
                addPerson(m);
            }

            // emigrations
            populationF.keySet().removeAll(e.getEmigrationsFemale());
            populationM.keySet().removeAll(e.getEmigrationsMale());

            // deaths
            populationF.keySet().removeAll(e.getDeathsFemale());
            populationM.keySet().removeAll(e.getDeathsMale());

            // todo check if immediately leaving entities show up in toString()
            time++;
            return makeLine(time, e);
        } else {
            time++;
            return makeLine(time, new Event());
        }
    }

    private String makeLine(int timeStamp, Event e) {
        return String.format("%d, %d, %d, %s\n", timeStamp, populationF.size(), populationM.size(), e.toString());
    }

    private void addImmigrations(int timestamp, Event e) {
        double probFemale = 0.5; // TODO: really 50:50?
        // compute how many immigrations happen in timestep
        while (cumulativeImmigrationTime < timestamp) {
            double lambda = SimulationParameters.affineLinear(timestamp, immigrationRate, slopeImmigrationRate);
            // todo: last immigration actually should happen in next timestep
            cumulativeImmigrationTime += randomExp(lambda);
            if (randomUnif() < probFemale) {
                e.scheduleWomanImmigration();
            } else {
                e.scheduleManImmigration();
            }
        }
    }
}
