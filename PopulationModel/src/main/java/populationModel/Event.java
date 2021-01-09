package populationModel;

import populationModel.person.Man;
import populationModel.person.Person;
import populationModel.person.Woman;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * holds information about Events, which are scheduled to happen within a time step
 */
public class Event {
    public static final String HEADER = "births, immigrationsF, immigrationsM, deathsF, deathsM, emigrationsF, emigrationsM";
    private int births = 0;
    private int immigrationsFemale = 0;
    private int immigrationsMale = 0;

    private final List<Woman> deathsFemale;
    private final List<Man> deathsMale;
    private final List<Woman> emigrationsFemale;
    private final List<Man> emigrationsMale;

    public Event() {
        emigrationsMale = new ArrayList<>();
        deathsFemale = new ArrayList<>();
        deathsMale = new ArrayList<>();
        emigrationsFemale = new ArrayList<>();
    }

    /**
     * increase number of scheduled births by 1
     */
    public void addBirth() {
        births++;
    }

    /**
     * add scheduled death of person
     *
     * @param w populationModel.person.Person
     */
    public void addDeath(Woman w) {
        deathsFemale.add(w);
    }

    public void addDeath(Man p) {
        deathsMale.add(p);
    }

    /**
     * add scheduled emigration of person
     *
     * @param p populationModel.person.Person
     */
    public void addEmigration(Man p) {
        emigrationsMale.add(p);
    }

    public void addEmigration(Woman p) {
        emigrationsFemale.add(p);
    }

    /**
     * add scheduled immigration of person
     *
     * @param female boolean specifying gender
     */
    public void addImmigration(boolean female) {
        if (female) {
            immigrationsFemale++;
        } else {
            immigrationsMale++;
        }
    }

    public int getBirths() {
        return births;
    }

    public int getImmigrationsFemale() {
        return immigrationsFemale;
    }

    public int getImmigrationsMale() {
        return immigrationsMale;
    }

    public List<Woman> getDeathsFemale() {
        return deathsFemale;
    }

    public List<Man> getDeathsMale() {
        return deathsMale;
    }

    public List<Woman> getEmigrationsFemale() {
        return emigrationsFemale;
    }

    public List<Man> getEmigrationsMale() {
        return emigrationsMale;
    }

    @Override
    public String toString() {
        return births +
                ", " + immigrationsFemale +
                ", " + immigrationsMale +
                ", " + deathsFemale.size() +
                ", " + deathsMale.size() +
                ", " + emigrationsFemale.size() +
                ", " + emigrationsMale.size();
    }
}
