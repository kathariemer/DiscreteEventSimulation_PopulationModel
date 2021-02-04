package populationModel;

import populationModel.person.Man;
import populationModel.person.Woman;

import java.util.ArrayList;

import static populationModel.util.RandomGenerator.randomUnif;

/**
 * Container which manages all TimeUnits and associates them to a unique timestep
 */
public class EventHistory {
    private final TimeUnit[] eventList;

    /**
     * initialize all time steps
     * @param size number of time steps
     */
    public EventHistory(int size) {
        eventList = new TimeUnit[size];
        for (int i = 0; i < eventList.length; i++) {
            eventList[i] = new TimeUnit();
        }
    }

    /**
     * add life events (i.e. either death or emigration) scheduled for a man
     *
     * @param person man
     */
    public void addEvents(Man person) {
        TimeUnit e = getTimeUnit(person.exitDate());
        if (e != null) {
            if (person.exitType() == Action.DEATH) {
                e.scheduleOneDeath(person);
            } else {
                e.scheduleOneEmigration(person);
            }
        }
    }

    /**
     * add life events (births and either death or emigration) scheduled for a woman
     *
     * @param w woman
     */
    public void addEvents(Woman w) {
        TimeUnit e = getTimeUnit(w.exitDate());
        if (e != null) {
            if (w.exitType() == Action.DEATH) {
                e.scheduleOneDeath(w);
            } else {
                e.scheduleOneEmigration(w);
            }
        }
        addBirths(w.getBirthTimes());
    }

    /**
     * add scheduled births and randomly compute gender
     *
     * @param timestamp of scheduled births
     */
    public void addBirths(ArrayList<Integer> timestamp) {
        for (int t : timestamp) {
            TimeUnit timeUnit = getTimeUnit(t);
            if (timeUnit != null) {
                if (randomUnif() < 0.5) {
                    timeUnit.scheduleGirlBirth();
                } else {
                    timeUnit.scheduleBoyBirth();
                }
            }
        }
    }

    /**
     * @param t associated time
     * @return TimeUnit, i.e. all events scheduled to happen at time t
     */
    public TimeUnit getTimeUnit(int t) {
        if (t >= 0 && t < eventList.length) {
            return eventList[t];
        }
        return null;
    }

    /**
     * delete an event scheduled at the given time step
     * @param t a time step
     */
    public void deleteTimeUnit(int t) {
        if (t >= 0 && t < eventList.length) {
            eventList[t] = null;
        }
    }

    /**
     * @return number of time steps
     */
    public int getDuration() {
        return eventList.length;
    }
}
