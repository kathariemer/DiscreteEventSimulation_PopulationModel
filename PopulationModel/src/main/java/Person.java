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

    boolean isFemale();
    boolean isMale();

    String toString();
}
