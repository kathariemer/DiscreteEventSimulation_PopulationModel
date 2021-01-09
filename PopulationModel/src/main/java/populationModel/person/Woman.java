package populationModel.person;

import populationModel.Action;
import populationModel.util.SimulationParameters;

import java.util.ArrayList;
import java.util.Objects;

import static populationModel.util.RandomGenerator.randomExp;

public class Woman implements Person{
    private static int femaleID = 0;

    private final int id;
    private final Action exit;
    private final int exitTime;
    private final int birthTime;
    private final ArrayList<Integer> birthTimes = new ArrayList<Integer>();

    /**
     * create a new woman and schedule her life events
     * @param timestamp timestamp of creation
     * @param params SimulationParameters with initialized birth rate
     */
    public Woman(int timestamp, SimulationParameters params) {
        id = femaleID;
        femaleID++;

        birthTime = timestamp;
        double death = randomExp(params.getDeathRate(timestamp));
        double emigration = randomExp(params.getEmigrationRate(timestamp));
        if (death < emigration) {
            exit = Action.DEATH;
            exitTime = timestamp + (int)death;
        } else {
            exit = Action.EMIGRATION;
            exitTime = timestamp + (int)emigration;
        }

        // update birthTimes ArrayList:
        scheduleBirths(timestamp, params.getBirthRate(timestamp));
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
        double totalTime = randomExp(birthRate);

        // while next birth happens while woman in system: schedule next birth
        while (totalTime < exitTime-timestamp) {
            birthTimes.add(timestamp + (int)totalTime);
            totalTime += randomExp(birthRate);
        }
        birthTimes.trimToSize();
    }

    public int getNumberOfChildren() {
        return birthTimes.size();
    }

    public String toString() {
        return "F: " + exitType() + " - " + exitDate() + "Children" + getNumberOfChildren();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Woman woman = (Woman) o;
        return id == woman.id;
    }

    @Override
    public int getBirthTime() {
        return birthTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
