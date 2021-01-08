import java.util.ArrayList;

public class Woman implements Person{
    public static int femaleID = 0;
    private static RandomGenerator rand = new RandomGenerator();

    private int id;
    private Action exit;
    private int exitTime;
    private ArrayList<Integer> birthTimes = new ArrayList<Integer>();
    private double lambdaBirth;

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

        lambdaBirth = birthrate;
        scheduleBirths(timestamp);
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

    public double nextBirth() {
        return rand.randomExp(lambdaBirth);
    }

    private void scheduleBirths(int timestamp) {
        double totalTime = nextBirth();
        while (totalTime < exitTime-timestamp) {
            birthTimes.add(timestamp + (int)totalTime);
            totalTime += nextBirth();
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
