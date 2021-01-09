package populationModel.person;

import populationModel.Action;
import populationModel.util.RandomGenerator;

import static populationModel.util.RandomGenerator.randomExp;

public class Man implements Person{
    public static int maleID = 0;
    private int id;
    private Action exit;
    private int exitTime;

    /**
     * create new man and schedule his one life event
     * @param timestamp
     * @param deathRate
     * @param emRate
     */
    public Man(int timestamp, double deathRate, double emRate) {
        id = maleID;
        maleID++;

        double death = randomExp(deathRate);
        double emigration = randomExp(emRate);
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

    @Override
    public boolean isFemale() {
        return false;
    }

    @Override
    public boolean isMale() {
        return true;
    }

    public String toString() {
        return "M: " + exitType() + " - " + exitDate();
    }

}