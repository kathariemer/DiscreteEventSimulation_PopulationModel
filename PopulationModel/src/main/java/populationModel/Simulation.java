package populationModel;

import populationModel.person.Man;
import populationModel.person.Woman;
import populationModel.util.ImmigrationParameters;
import populationModel.util.PopulationParameters;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static populationModel.util.RandomGenerator.*;

/**
 * Class that gets initialized with parameters and allows to step through simulation runs
 */
public class Simulation implements Iterator<int[]> {
    public static final String HEADER = String.format("time, populationF, populationM, %s", TimeUnit.HEADER);

    // time <= duration
    private int time;
    // fixed value
    private int duration;
    private EventHistory eventList;

    private Set<Integer> populationF;
    private Set<Integer> populationM;

    private final PopulationParameters womenParams;
    private final PopulationParameters menParams;
    private final ImmigrationParameters immigrationParameters;

    private double cumulativeImmigrationTime;

    /**
     * read parameters from input file, where each line starts with the key name, followed by an equality symbol and
     * then the value. In order to ensure correct parsing, no punctuation is accepted.
     * <p>
     * save all parameters for the simulation and initialize the population
     *
     * @param inputFile path to input file
     */
    public Simulation(String inputFile) throws IOException, NumberFormatException {
        // create and load default properties
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream(inputFile);
        properties.load(inputStream);
        inputStream.close();


        String fieldName = "time_steps";
        String propertyValue = properties.getProperty(fieldName);
        try {
            // init parameters
            duration = Integer.parseInt(propertyValue);

            // immigration parameters
            propertyValue = properties.getProperty(fieldName = "lambda_immi");
            double immigrationRate = Double.parseDouble(propertyValue);

            propertyValue = properties.getProperty(fieldName = "slope_immi_rate");
            double slopeImmigrationRate = Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "imm_prop_female", "0.5");
            double propF = Double.parseDouble(propertyValue);
            // propertyValue = properties.getProperty(fieldName = "imm_mean_age");
            // double meanAge = Double.parseDouble(propertyValue);
            // propertyValue = properties.getProperty(fieldName = "imm_sd_age");
            // double sdAge = Double.parseDouble(propertyValue);
            immigrationParameters = new ImmigrationParameters(immigrationRate, slopeImmigrationRate,
                    propF, 0, 0);

            // population parameters women

            propertyValue = properties.getProperty(fieldName = "init_f");
            int initialPopulationSizeWomen = Integer.parseInt(propertyValue);
            propertyValue = properties.getProperty(fieldName = "mu_death_f");
            double deathRateWomen = 1 / Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "slope_death_rate_f");
            double slopeDeathRateWomen = Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "mu_emigration");
            double emigrationRateWomen = 1 / Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "slope_emigration_rate");
            double slopeEmigrationRateWomen = Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "mu_birth");
            double birthRate = 1 / Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "slope_birth_rate");
            double slopeBirthRate = Double.parseDouble(propertyValue);

            womenParams = new PopulationParameters(initialPopulationSizeWomen,
                    deathRateWomen, slopeDeathRateWomen,
                    emigrationRateWomen, slopeEmigrationRateWomen,
                    birthRate, slopeBirthRate);

            // population parameters men
            propertyValue = properties.getProperty(fieldName = "init_m");
            int initialPopulationSizeMen = Integer.parseInt(propertyValue);
            propertyValue = properties.getProperty(fieldName = "mu_death_m");
            double deathRateMen = 1 / Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "slope_death_rate_m");
            double slopeDeathRateMen = Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "mu_emigration");
            double emigrationRateMen = 1 / Double.parseDouble(propertyValue);
            propertyValue = properties.getProperty(fieldName = "slope_emigration_rate");
            double slopeEmigrationRateMen = Double.parseDouble(propertyValue);

            menParams = new PopulationParameters(initialPopulationSizeMen,
                    deathRateMen, slopeDeathRateMen,
                    emigrationRateMen, slopeEmigrationRateMen);
        } catch (NullPointerException e) {
            throw new NullPointerException(String.format("Required field <%s> not found in input file. Check spelling.", fieldName));
        } catch (NumberFormatException e) {
            if (propertyValue == null) {
                throw new NullPointerException(String.format("Required field <%s> not found in input file. Check spelling.", fieldName));
            } else {
                throw new NumberFormatException(String.format("Cannot parse number <%s> in field <%s>.", propertyValue, fieldName));
            }
        }

        reset();
    }

    /**
     * after changed parameters, re-initialize the simulation
     * Reset time = 0
     * population = new HashSet
     * eventList = new EventHistory
     * create initial population
     */
    public void reset() {
        time = 0;
        populationF = new HashSet<>(womenParams.getInitialPopulationSize());
        populationM = new HashSet<>(menParams.getInitialPopulationSize());
        eventList = new EventHistory(duration);
        initPopulation(womenParams.getInitialPopulationSize(), menParams.getInitialPopulationSize());
    }

    /**
     * initialize population
     *
     * @param sizeF number of women
     * @param sizeM number of men
     */
    private void initPopulation(int sizeF, int sizeM) {
        int time0 = 0;
        cumulativeImmigrationTime = (float) (time0 + randomExp(immigrationParameters.getImmigrationRate(time0)));
        // todo: maybe change people's ages
        for (int i = 0; i < sizeF; i++) {
            Woman w = new Woman(time0, womenParams);
            populationF.add(w.getID());
            eventList.addEvents(w);
        }

        for (int i = 0; i < sizeM; i++) {
            Man m = new Man(time0, menParams);
            populationM.add(m.getID());
            eventList.addEvents(m);
        }
    }

    @Override
    public boolean hasNext() {
        // don't compare with attribute duration, which might
        // have changed already in preparation for a
        // new simulation run
        return time < eventList.getDuration();
    }

    /**
     * compute a time step and advance attribute "time" by 1
     *
     * @return simulation statistics representing in a comma separated format
     */
    @Override
    public int[] next() {
        if (time == 0) {
            return currentStats(time++);
        }
        TimeUnit e = eventList.getTimeUnit(time);
        if (e != null) {
            addImmigrations(time, e);

            // births and immigrations women
            for (int i = 0; i < e.getBirthsFemale(); i++) {
                Woman w = new Woman(time, womenParams);
                integrateWoman(w);
            }
            for (int j = 0; j < e.getImmigrationsFemale(); j++) {
                int birthYear = time - immigrationParameters.randomAge();
                integrateWoman(new Woman(birthYear, womenParams));
            }

            // births and immigrations men
            for (int i = 0; i < e.getBirthsMale(); i++) {
                Man m = new Man(time, menParams);
                integrateMan(m);
            }
            for (int i = 0; i < e.getImmigrationsMale(); i++) {
                int birthYear = time - immigrationParameters.randomAge();
                integrateMan(new Man(birthYear, menParams));
            }

            // emigrations
            populationF.removeAll(e.getEmigrationsFemale());
            populationM.removeAll(e.getEmigrationsMale());

            // deaths
            populationF.removeAll(e.getDeathsFemale());
            populationM.removeAll(e.getDeathsMale());

        } else {
            e = new TimeUnit();
        }
        // todo check if immediately leaving entities show up in toString()
        // delete history for performance reasons - however THAT MEANS THAT EVENTS
        // SCHEDULED AT THE CURRENT TIME UNIT CANNOT BE ADDED
        eventList.deleteTimeUnit(time);
        return currentStats(time++, e);
    }

    /**
     * add woman to population and add life events to eventList
     *
     * @param w woman
     */
    private void integrateWoman(Woman w) {
        populationF.add(w.getID());
        eventList.addEvents(w);
    }

    /**
     * add man to population and add life event to event list
     *
     * @param m man
     */
    private void integrateMan(Man m) {
        populationM.add(m.getID());
        eventList.addEvents(m);
    }

    /**
     * @param timeStamp current time
     * @param e         events happening at time timeStamp
     * @return line of comma-separated statistics
     */
    private String makeLine(int timeStamp, TimeUnit e) {
        return String.format("%d, %d, %d, %s\n", timeStamp, populationF.size(), populationM.size(), e.toString());
    }

    /**
     * get population size
     * @param timeStamp current timestamp
     * @return an array of initial population statistics
     */
    private int[] currentStats(int timeStamp) {
        int[] stats = new int[TimeUnit.STATCOUNT + 3];
        stats[0] = timeStamp;
        stats[1] = populationF.size();
        stats[2] = populationM.size();
        return stats;
    }

    /**
     * get population size and birth/death, migration counts
     * @param timeStamp current timestamp
     * @param e TimeUnit event associated with the timestamp
     * @return population size and birth/death/migration statistics
     */
    private int[] currentStats(int timeStamp, TimeUnit e) {
        int[] stats = new int[TimeUnit.STATCOUNT + 3];
        stats[0] = timeStamp;
        stats[1] = populationF.size();
        stats[2] = populationM.size();
        stats[3] = e.getBirthsFemale();
        stats[4] = e.getBirthsMale();
        stats[5] = e.getDeathsFemale().size();
        stats[6] = e.getDeathsMale().size();
        stats[7] = e.getImmigrationsFemale() + e.getImmigrationsMale();
        stats[8] = e.getEmigrationsFemale().size() + e.getEmigrationsMale().size();
        return stats;
    }

    /**
     * compute number of immigrations in TimeUnit e
     *
     * @param timestamp current time
     * @param e         time unit
     */
    private void addImmigrations(int timestamp, TimeUnit e) {
        // compute how many immigrations happen in timestep
        double lambda = immigrationParameters.getImmigrationRate(timestamp);

        //cumulativeImmigrationTime += randomExp(lambda);
        while (cumulativeImmigrationTime < timestamp) {
            if (randomUnif() < immigrationParameters.getProportionF()) {
                e.scheduleWomanImmigration();
            } else {
                e.scheduleManImmigration();
            }
            cumulativeImmigrationTime += randomExp(lambda);
        }
    }

    // setters

    public boolean setDuration(int t) {
        boolean b = !hasNext();
        if (b) {
            duration = t;
        }
        return b;
    }

    // getters

    public PopulationParameters getWomenParams() {
        return womenParams;
    }

    public PopulationParameters getMenParams() {
        return menParams;
    }

    public ImmigrationParameters getImmigrationParameters() {
        return immigrationParameters;
    }

    @Override
    public String toString() {
        return "Simulation{" +
                "duration=" + duration +
                ", womenParams=" + womenParams +
                ", menParams=" + menParams +
                ", immigrationParameters=" + immigrationParameters +
                '}';
    }
}
