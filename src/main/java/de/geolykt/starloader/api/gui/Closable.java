package de.geolykt.starloader.api.gui;

public interface Closable {

    /**
     * Closes the object from view.
     * If the object expects User input,
     * then a call to this method should be considered as cancelling the request.
     *
     */
    public void close();
}
