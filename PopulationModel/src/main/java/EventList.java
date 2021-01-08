import java.util.ArrayList;
import java.util.HashMap;

public class EventList {
    private HashMap<Integer, Event> eventlist = new HashMap<Integer, Event>();

    /**
     * add life events scheduled for a man
     * @param person man
     */
    public void addEvents(Man person) {
       Event e = getEvent(person.exitDate());
       if (person.exitType() == Action.DEATH) {
           e.addDeath(person);
       } else {
           e.addEmigration(person);
       }
    };

    /**
     * add life events scheduled for a woman
     * @param w woman
     */
    public void addEvents(Woman w) {
        Event e = getEvent(w.exitDate());
        if (w.exitType() == Action.DEATH) {
            e.addDeath(w);
        } else {
            e.addEmigration(w);
        }
        addBirths(w.getBirthTimes());
    };

    /**
     * add scheduled births
     * @param timestamp of scheduled births
     */
    public void addBirths(ArrayList<Integer> timestamp) {
        for (int t:timestamp) getEvent(t).addBirth();
    }

    /**
     * get Event associated with timestamp
     * @param t timestamp
     * @return a non-null Event
     */
    private Event getEvent(Integer t) {
        Event e = eventlist.get(t);
        if (e == null) {
            e = new Event();
            eventlist.put(t, e);
        }
        return e;
    }

    public Event getScheduledEvents(int t) {
        return eventlist.get(t);
    }
}
