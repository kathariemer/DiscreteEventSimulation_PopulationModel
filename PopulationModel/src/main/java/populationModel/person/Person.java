package populationModel.person;

import populationModel.Action;

/**
 * A person has an ID and a time of death (exclusive) or emigration
 */
public interface Person {

    /**
     * get ID associated with person; Java's max. int is ~ 2*10^9
     * @return person ID
     */
    int getID();

    /**
     * get timestamp of scheduled exit from population
     * @return timestamp
     */
    int exitDate();

    /**
     * @return emigration or death action
     */
    Action exitType();

    String toString();
}
