package populationModel;

import populationModel.person.Man;
import populationModel.person.Woman;

import java.util.ArrayList;

public class EventHistory {
    private Event[] eventList;

    public EventHistory(int size) {
        eventList = new Event[size];
        for (int i = 0; i < eventList.length; i++) {
            eventList[i] = new Event();
        }
    }

    /**
     * add life events scheduled for a man
     *
     * @param person man
     */
    public void addEvents(Man person) {
        Event e = getEvent(person.exitDate());
        if (e != null) {
            if (person.exitType() == Action.DEATH) {
                e.scheduleOneDeath(person);
            } else {
                e.scheduleOneEmigration(person);
            }
        }
    }

    ;

    /**
     * add life events scheduled for a woman
     *
     * @param w woman
     */
    public void addEvents(Woman w) {
        Event e = getEvent(w.exitDate());
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
     * add scheduled births
     *
     * @param timestamp of scheduled births
     */
    public void addBirths(ArrayList<Integer> timestamp) {
        for (int t : timestamp) {
            Event event = getEvent(t);
            if (event != null) {
                event.scheduleOneBirth();
            }
        }
    }

    public Event getEvent(int t) {
        if (t >= 0 && t < eventList.length) {
            return eventList[t];
        }
        return null;
    }

    public int getDuration() {
        return eventList.length;
    }
}
