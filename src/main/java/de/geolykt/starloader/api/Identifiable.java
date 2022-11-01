package de.geolykt.starloader.api;

import java.util.concurrent.atomic.AtomicInteger;

public interface Identifiable {

    /**
     * Obtains the usually unique (numeric) identifier of the object. There should
     * be no duplicates. However due to the way the game operates, there might be
     * duplicates, which can have unintended consequences. But if you have control
     * over this UID, please don't copy the game, which uses a {@link Math#random()}
     * operation. Instead a running counter (preferably an {@link AtomicInteger})
     * should be employed.
     *
     * @return The UID of the empire
     */
    public int getUID();
}
