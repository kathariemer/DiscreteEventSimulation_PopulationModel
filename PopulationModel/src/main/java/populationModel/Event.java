package populationModel;

import populationModel.person.Person;

import java.util.LinkedList;

/**
 * holds information about Events, which are scheduled to happen within a time step
 */
public class Event {
    private int births = 0;
    private int immigrationsFemale = 0;
    private int immigrationsMale = 0;
    private LinkedList<Integer> deathsFemale = new LinkedList();
    private LinkedList<Integer> deathsMale = new LinkedList();
    private LinkedList<Integer> emigrationsFemale = new LinkedList();
    private LinkedList<Integer> emigrationsMale = new LinkedList();

    /**
     * increase number of scheduled births by 1
     */
    public void addBirth() {
        births++;
    }

    /**
     * add scheduled death of person
     * @param p populationModel.person.Person
     */
    public void addDeath(Person p) {
        if (p.isFemale()) {
            deathsFemale.add(p.getID());
        } else {
            deathsMale.add(p.getID());
        }
    }

    /**
     * add scheduled emigration of person
     * @param p populationModel.person.Person
     */
    public void addEmigration(Person p) {
        if (p.isFemale()) {
            emigrationsFemale.add(p.getID());
        } else {
            emigrationsMale.add(p.getID());
        }
    }

    /**
     * add scheduled immigration of person
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

    public LinkedList<Integer> getDeathsFemale() {
        return deathsFemale;
    }

    public LinkedList<Integer> getDeathsMale() {
        return deathsMale;
    }

    public LinkedList<Integer> getEmigrationsFemale() {
        return emigrationsFemale;
    }

    public LinkedList<Integer> getEmigrationsMale() {
        return emigrationsMale;
    }

    public static String getHeader(){
        return "births, immigrationsF, immigrationsM, deathsF, deathsM, emigrationsF, emigrationsM";
    }

    @Override
    public String toString() {
        return births +
                ", " + immigrationsFemale +
                ", " + immigrationsMale +
                ", " + deathsFemale.size() +
                ", " + deathsMale.size() +
                ", " + emigrationsFemale.size() +
                ", " + emigrationsMale.size() ;
    }
}
