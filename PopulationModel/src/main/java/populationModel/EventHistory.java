package populationModel;

import populationModel.person.Man;
import populationModel.person.Woman;

import java.util.ArrayList;

import static populationModel.util.RandomGenerator.randomUnif;

public class EventHistory {
    private final TimeUnit[] eventList;

    public EventHistory(int size) {
        eventList = new TimeUnit[size];
        for (int i = 0; i < eventList.length; i++) {
            eventList[i] = new TimeUnit();
        }
    }

    /**
     * add life events scheduled for a man
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
     * add life events scheduled for a woman
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
     * @return TimeUnit, i.e. all events sceduled to happen at time t
     */
    public TimeUnit getTimeUnit(int t) {
        if (t >= 0 && t < eventList.length) {
            return eventList[t];
        }
        return null;
    }

    public void deleteTimeUnit(int t) {
        if (t >= 0 && t < eventList.length) {
            eventList[t] = null;
        }
    }

    public int getDuration() {
        return eventList.length;
    }
}
