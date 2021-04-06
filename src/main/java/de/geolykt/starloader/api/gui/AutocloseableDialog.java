package de.geolykt.starloader.api.gui;

public interface AutocloseableDialog extends Closable {

    /**
     * Obtains the time at which the dialog will be closed automatically, or -1 if
     * automatic closing is not done. The time is relative to the starting point of
     * {@link System#currentTimeMillis()} and is in milliseconds.
     */
    public long getAutocloseTime();
}
