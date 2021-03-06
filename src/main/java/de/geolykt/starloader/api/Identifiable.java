package de.geolykt.starloader.api;

public interface Identifiable {

    /**
     * Obtains the usually unique (numeric) identifier of the object. There should be no duplicates.
     * However due to the way the game operates, there might be duplicates, which can have unintended consquences.
     * @return The UID of the empire
     */
    public int getUID();

}
