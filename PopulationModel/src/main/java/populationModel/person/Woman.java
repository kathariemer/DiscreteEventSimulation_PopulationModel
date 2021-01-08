package populationModel.person;

import populationModel.Action;
import populationModel.RandomGenerator;

import java.util.ArrayList;

public class Woman implements Person{
    public static int femaleID = 0;
    private static RandomGenerator rand = new RandomGenerator();

    private int id;
    private Action exit;
    private int exitTime;
    private ArrayList<Integer> birthTimes = new ArrayList<Integer>();

    /**
     * create a new woman and schedule her life events
     * @param timestamp timestamp of creation
     * @param deathRate inverse of life expectancy
     * @param emRate inverse of ???
     * @param birthrate inverse of ???
     */
    public Woman(int timestamp, double deathRate, double emRate, double birthrate) {
        id = femaleID;
        femaleID++;

        double death = rand.randomExp(deathRate);
        double emigration = rand.randomExp(emRate);
        if (death < emigration) {
            exit = Action.DEATH;
            exitTime = timestamp + (int)death;
        } else {
            exit = Action.EMIGRATION;
            exitTime = timestamp + (int)emigration;
        }

        // update birthTimes ArrayList:
        scheduleBirths(timestamp, birthrate);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int exitDate() {
        return exitTime;
    }

    @Override
    public Action exitType() {
        return exit;
    }

    public ArrayList<Integer> getBirthTimes() {return birthTimes;}

    private void scheduleBirths(int timestamp, double birthRate) {
        // get time to first birth
        double totalTime = rand.randomExp(birthRate);

        // while next birth happens while woman in system: schedule next birth
        while (totalTime < exitTime-timestamp) {
            birthTimes.add(timestamp + (int)totalTime);
            totalTime += rand.randomExp(birthRate);
        }
        birthTimes.trimToSize();
    }

    @Override
    public boolean isFemale() {
        return true;
    }

    @Override
    public boolean isMale() {
        return false;
    }

    public int getNumberOfChildren() {
        return birthTimes.size();
    }

    public String toString() {
        return "F: " + exitType() + " - " + exitDate() + "Children" + getNumberOfChildren();
    }
}
