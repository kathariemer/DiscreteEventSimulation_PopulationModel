package populationModel;

import populationModel.person.Man;
import populationModel.person.Woman;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds information about events, which are scheduled to happen within a specific time step
 */
public class TimeUnit {
    public static final String HEADER = "birthsF, birthsM, deathsF, deathsM, immigrations, emigrations";
    public static final int STATCOUNT = 6;
    private int birthsFemale = 0;
    private int birthsMale = 0;
    private int immigrationsFemale = 0;
    private int immigrationsMale = 0;

    private final List<Integer> deathsFemale;
    private final List<Integer> deathsMale;
    private final List<Integer> emigrationsFemale;
    private final List<Integer> emigrationsMale;

    public TimeUnit() {
        emigrationsMale = new ArrayList<>();
        deathsFemale = new ArrayList<>();
        deathsMale = new ArrayList<>();
        emigrationsFemale = new ArrayList<>();
    }

    /**
     * increase number of scheduled births by 1
     */
    public void scheduleGirlBirth() {
        birthsFemale++;
    }

    public void scheduleBoyBirth() {
        birthsMale++;
    }

    /**
     * add scheduled death of person
     *
     * @param w populationModel.person.Person
     */
    public void scheduleOneDeath(Woman w) {
        deathsFemale.add(w.getID());
    }

    public void scheduleOneDeath(Man p) {
        deathsMale.add(p.getID());
    }

    /**
     * add scheduled emigration of person
     *
     * @param p populationModel.person.Person
     */
    public void scheduleOneEmigration(Man p) {
        emigrationsMale.add(p.getID());
    }

    public void scheduleOneEmigration(Woman p) {
        emigrationsFemale.add(p.getID());
    }

    /**
     * add scheduled immigration of one woman
     */
    public void scheduleWomanImmigration() {
        immigrationsFemale++;
    }

    /**
     * add scheduled immigration of one man
     */
    public void scheduleManImmigration() {
        immigrationsMale++;
    }

    public int getBirthsFemale() {
        return birthsFemale;
    }

    public int getBirthsMale() {
        return birthsMale;
    }

    public int getImmigrationsFemale() {
        return immigrationsFemale;
    }

    public int getImmigrationsMale() {
        return immigrationsMale;
    }

    public List<Integer> getDeathsFemale() {
        return deathsFemale;
    }

    public List<Integer> getDeathsMale() {
        return deathsMale;
    }

    public List<Integer> getEmigrationsFemale() {
        return emigrationsFemale;
    }

    public List<Integer> getEmigrationsMale() {
        return emigrationsMale;
    }

    @Override
    public String toString() {
        return birthsFemale +
                ", " + birthsMale +
                ", " + deathsFemale.size() +
                ", " + deathsMale.size() +
                ", " + (immigrationsFemale + immigrationsMale) +
                ", " + (emigrationsFemale.size() + emigrationsMale.size())
                ;
    }
}
