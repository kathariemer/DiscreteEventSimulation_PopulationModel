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

public class Simulation implements Iterator<int[]> {
    public static final String HEADER = String.format("time, populationF, populationM, %s", TimeUnit.HEADER);

    // time <= duration
    private int time;
    // fixed value
    private int duration;
    private EventHistory eventList;

    private Set<Woman> populationF;
    private Set<Man> populationM;

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

        // init parameters
        duration = Integer.parseInt(properties.getProperty("timeSteps"));

        // immigration parameters
        double immigrationRate = 1 / Double.parseDouble(properties.getProperty("inv_immRate"));
        double slopeImmigrationRate = Double.parseDouble(properties.getProperty("slope_immRate"));
        double propF = Double.parseDouble(properties.getProperty("imm_prop_female"));
        double meanAge = Double.parseDouble(properties.getProperty("imm_mean_age"));
        double sdAge = Double.parseDouble(properties.getProperty("imm_sd_age"));
        immigrationParameters = new ImmigrationParameters(immigrationRate, slopeImmigrationRate,
                propF, meanAge, sdAge);

        // population parameters women
        int initialPopulationSizeWomen = Integer.parseInt(properties.getProperty("init_f"));
        double deathRateWomen = 1 / Double.parseDouble(properties.getProperty("inv_deathRate_f"));
        double slopeDeathRateWomen = Double.parseDouble(properties.getProperty("slope_deathRate_f"));
        double emigrationRateWomen = 1 / Double.parseDouble(properties.getProperty("inv_emRate"));
        double slopeEmigrationRateWomen = Double.parseDouble(properties.getProperty("slope_emRate"));
        double birthRate = 1 / Double.parseDouble(properties.getProperty("inv_birthRate"));
        double slopeBirthRate = Double.parseDouble(properties.getProperty("slope_birthRate"));

        womenParams = new PopulationParameters(initialPopulationSizeWomen,
                deathRateWomen, slopeDeathRateWomen,
                emigrationRateWomen, slopeEmigrationRateWomen,
                birthRate, slopeBirthRate);

        // population parameters men
        int initialPopulationSizeMen = Integer.parseInt(properties.getProperty("init_m"));
        double deathRateMen = 1 / Double.parseDouble(properties.getProperty("inv_deathRate_m"));
        double slopeDeathRateMen = Double.parseDouble(properties.getProperty("slope_deathRate_m"));
        double emigrationRateMen = 1 / Double.parseDouble(properties.getProperty("inv_emRate"));
        double slopeEmigrationRateMen = Double.parseDouble(properties.getProperty("slope_emRate"));

        menParams = new PopulationParameters(initialPopulationSizeMen,
                deathRateMen, slopeDeathRateMen,
                emigrationRateMen, slopeEmigrationRateMen);

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
        cumulativeImmigrationTime = (float) time0 - 1 + randomExp(immigrationParameters.getImmigrationRate(time0));
        // todo: maybe change people's ages
        for (int i = 0; i < sizeF; i++) {
            Woman w = new Woman(time0, womenParams);
            populationF.add(w);
        }
        populationF.forEach(eventList::addEvents);

        for (int i = 0; i < sizeM; i++) {
            Man m = new Man(time0, menParams);
            populationM.add(m);
        }
        populationM.forEach(eventList::addEvents);
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
        TimeUnit e = eventList.getTimeUnit(time);
        if (e != null) {
            addImmigrations(time, e);

            // birth
            for (int i = 0; i < e.getBirthsFemale(); i++) {
                Woman w = new Woman(time, womenParams);
                integrateWoman(w);
            }
            for (int i = 0; i < e.getBirthsMale(); i++) {
                Man m = new Man(time, menParams);
                integrateMan(m);
            }
            // immigrations
            // add "age", i.e. assume that person has age ~ N(30, 10)???
            for (int j = 0; j < e.getImmigrationsFemale(); j++) {
                int birthYear = time - immigrationParameters.randomAge();
                integrateWoman(new Woman(birthYear, womenParams));
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
        populationF.add(w);
        eventList.addEvents(w);
    }

    /**
     * add man to population and add life event to event list
     *
     * @param m man
     */
    private void integrateMan(Man m) {
        populationM.add(m);
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

    private int[] currentStats(int timeStamp) {
        int[] stats = new int[TimeUnit.STATCOUNT + 3];
        stats[0] = timeStamp;
        stats[1] = populationF.size();
        stats[2] = populationM.size();
        return stats;
    }

    private int[] currentStats(int timeStamp, TimeUnit e) {
        int[] stats = new int[TimeUnit.STATCOUNT + 3];
        stats[0] = timeStamp;
        stats[1] = populationF.size();
        stats[2] = populationM.size();
        stats[3] = e.getBirthsFemale();
        stats[4] = e.getBirthsMale();
        stats[5] = e.getImmigrationsFemale();
        stats[6] = e.getImmigrationsMale();
        stats[7] = e.getDeathsFemale().size();
        stats[8] = e.getDeathsMale().size();
        stats[9] = e.getEmigrationsFemale().size();
        stats[10] = e.getEmigrationsMale().size();
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

    public List<Integer> getAgeF(int t) {
        return populationF.stream().map(w -> t - w.getBirthTime()).collect(Collectors.toList());
    }

    public List<Integer> getAgeM(int t) {
        return populationM.stream().map(m -> t - m.getBirthTime()).collect(Collectors.toList());
    }

    public boolean setBirthrate(double rate) {
        boolean b = !hasNext();
        if (b) {
            womenParams.setBirthRate(rate);
        }
        return b;
    }

    public boolean setEmigrationRate(double rate) {
        boolean b = !hasNext();
        if (b) {
            womenParams.setEmigrationRate(rate);
            menParams.setEmigrationRate(rate);
        }
        return b;
    }

    public boolean setImmigrationRate(double rate) {
        boolean b = !hasNext();
        if (b) {
            immigrationParameters.setRate(rate);
        }
        return b;
    }

    public boolean setFemaleDeathRate(double rate) {
        boolean b = !hasNext();
        if (b) {
            womenParams.setDeathRate(rate);
        }
        return b;
    }

    public boolean setMaleDeathRate(double rate) {
        boolean b = !hasNext();
        if (b) {
            menParams.setDeathRate(rate);
        }
        return b;
    }

    public boolean setFemaleInitialPopulationSize(int size) {
        boolean b = !hasNext();
        if (b) {
            womenParams.setInitialPopulationSize(size);
        }
        return b;
    }

    public boolean setMaleInitialPopulationSize(int size) {
        boolean b = !hasNext();
        if (b) {
            menParams.setInitialPopulationSize(size);
        }
        return b;
    }

    public boolean setDuration(int t) {
        boolean b = !hasNext();
        if (b) {
            duration = t;
        }
        return b;
    }
}
