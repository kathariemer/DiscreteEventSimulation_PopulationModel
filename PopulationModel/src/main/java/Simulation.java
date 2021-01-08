import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Simulation {
    private final RandomGenerator rand = new RandomGenerator();
    private final boolean FEMALE = true;
    private final boolean MALE = false;

    private EventList eventlist = new EventList();

    private int[] p_init = new int[2];
    // todo: unnecessary to save people, consider not saving or saving only some statistic
    private HashMap<Integer, Woman> populationF = new HashMap<Integer, Woman>();
    private HashMap<Integer, Man> populationM = new HashMap<Integer, Man>();

    private double[] deathRates = new double[2];
    private double[] emigrationRates = new double[2];
    private double birthRate = 0.0;
    private double immRate = 0.0;

    // todo: add slope coefficients
    private final double coef_b = 0.0;

    public int timeSteps = 500;
    private double cumulativeImmigrationTime;

    /**
     * read parameters from input file, where each line starts with the key name, followed by an equality symbol and
     * then the value. In order to ensure correct parsing, no punctuation is accepted.
     * @param inputFile path to input file
     */
    public Simulation(String inputFile) {
        // create and load default properties
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(inputFile);
            properties.load(in);
            initParams(properties);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initParams(Properties p) {
        p_init[0] = Integer.parseInt(p.getProperty("init_f"));
        p_init[1] = Integer.parseInt(p.getProperty("init_m"));

        // todo add coefficients for linear function
        deathRates[0] = 1/Double.parseDouble(p.getProperty("inv_deathRate_f"));
        deathRates[1] = 1/Double.parseDouble(p.getProperty("inv_deathRate_m"));

        emigrationRates[0] = 1/Double.parseDouble(p.getProperty("inv_emRate_f"));
        emigrationRates[1] = 1/Double.parseDouble(p.getProperty("inv_emRate_m"));

        birthRate = 1/Double.parseDouble(p.getProperty("inv_birthRate"));
        immRate = 1/Double.parseDouble(p.getProperty("inv_immRate"));

        timeSteps = Integer.parseInt(p.getProperty("timeSteps"));
    }

    /**
     * add person to female or male population, depending in gender
     * and schedule associated life events
     * @param p Person
     */
    private void addPerson(Person p) {
        if (p.isFemale()) {
            Woman w = (Woman)p;
            eventlist.addEvents(w);
            populationF.put(w.getID(), w);
        } else {
            Man m = (Man)p;
            eventlist.addEvents(m);
            populationM.put(m.getID(), m);
        }
    }

    /**
     * Initialize a population of x women and y men
     * @param time0
     */
    public void init(int time0) throws IllegalArgumentException {
        cumulativeImmigrationTime = (float)time0;
        if (time0 != 0) {
            throw new IllegalArgumentException("please start your simulation at time 0.");
        }
        for (int i = 0; i < getInitialPopulationSize(FEMALE); i++) {
            Woman w = new Woman(time0, getDeathRate(FEMALE), getEmigrationRate(FEMALE), getBirthRate(time0));
            //System.out.print(w.getNumberOfChildren()+",");
            addPerson(w);
        }

        for (int i = 0; i < getInitialPopulationSize(MALE); i++) {
            Man m = new Man(time0, getDeathRate(MALE), getEmigrationRate(MALE));
            addPerson(m);
        }
    }

    /**
     * compute a time step
     * @param t current time
     * @return simulation statistics representing in a comma separated format
     */
    public String step(int t) {
        Event e = eventlist.getScheduledEvents(t);
        if (e != null) {
            // birth
            for (int i = 0; i < e.getBirths(); i++) {
                if (rand.randomUnif() < 0.5) {
                    Woman w = new Woman(t, getDeathRate(FEMALE), getEmigrationRate(FEMALE), getBirthRate(t));
                    addPerson(w);
                } else {
                    Man m = new Man(t, getDeathRate(MALE), getEmigrationRate(MALE));
                    addPerson(m);
                }
            }

            // immigrations
            addImmigrations(t, e);
            for (int j = 0; j < e.getImmigrationsFemale(); j++) {
                // TODO: add "age"? i.e. assume that person has age ~ N(30, 10)???
                Woman w = new Woman(t, getDeathRate(FEMALE), getEmigrationRate(FEMALE), getBirthRate(t));
                addPerson(w);
            }
            for (int i = 0; i < e.getImmigrationsMale(); i++) {
                Man m = new Man(t, getDeathRate(MALE), getEmigrationRate(MALE));
                addPerson(m);
            }

            // emigrations
            populationF.keySet().removeAll(e.getEmigrationsFemale());
            populationM.keySet().removeAll(e.getEmigrationsMale());

            // deaths
            populationF.keySet().removeAll(e.getDeathsFemale());
            populationM.keySet().removeAll(e.getDeathsMale());

            // todo check if immediately leaving entities show up in toString()
            return makeLine(t, e);
        } else {
            return makeLine(t, new Event());
        }
    }

    private String makeLine(int timeStamp, Event e) {
       return String.format("%d, %d, %d, %s\n", timeStamp, populationF.size(), populationM.size(), e.toString());
    }

    /**
     * @return comma separated column names
     */
    public static String getHeader() {
        return String.format("time, populationF, populationM, %s", Event.getHeader());
    }

    // get static values

    private int getInitialPopulationSize(boolean female) {
        return p_init[female?0:1];
    }

    public int getMaxSteps() {
        return timeSteps;
    }

    // get dynamic values
    // todo: add linear dependency on time

    private double getBirthRate(int t) {
        return birthRate + coef_b * t;
    }

    private double getDeathRate(boolean female) {
        return female?deathRates[0]:deathRates[1];
    }

    private double getEmigrationRate(boolean female) {
        return female?emigrationRates[0]:emigrationRates[1];
    }

    private void addImmigrations(int timestamp, Event e) {
        double probFemale = 0.5; // TODO: really 50:50?
        // todo: I don't get why immigration rate and emigration rates are so different :(
        while (cumulativeImmigrationTime < timestamp) {
            cumulativeImmigrationTime += rand.randomExp(immRate);
            e.addImmigration(rand.randomUnif() < probFemale);
        }
    }
}
