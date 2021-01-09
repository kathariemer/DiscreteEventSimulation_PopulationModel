package populationModel.person;

import populationModel.Action;
import populationModel.util.SimulationParameters;

import java.util.Objects;

import static populationModel.util.RandomGenerator.randomExp;

public class Man implements Person{
    public static int maleID = 0;
    private final int id;
    private final Action exit;
    private final int exitTime;

    /**
     * create new man and schedule his one life event
     * @param timestamp time of this person's birth
     * @param params SimulationParameters
     */
    public Man(int timestamp, SimulationParameters params) {
        id = maleID;
        maleID++;

        double death = randomExp(params.getDeathRate(timestamp));
        double emigration = randomExp(params.getEmigrationRate(timestamp));
        if (death < emigration) {
            exit = Action.DEATH;
            exitTime = timestamp + (int)death;
        } else {
            exit = Action.EMIGRATION;
            exitTime = timestamp + (int)emigration;
        }
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

    public String toString() {
        return "M: " + exitType() + " - " + exitDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Man man = (Man) o;
        return id == man.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
