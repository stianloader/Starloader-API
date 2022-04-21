package de.geolykt.starloader.api.gui;

public interface Closable {

    /**
     * Closes the object from view. If the object expects User input, then a call to
     * this method should be considered as cancelling the request.
     */
    public void close();

    /**
     * Checks whether the dialog was closed, or more specifically whether {@link #close()} was invoked on
     * this instance. This method may also return false if the instance wasn't opened/shown in the first place.
     *
     * @return True if the instance was closed, false otherwise
     * @since 2.0.0
     */
    public boolean isClosed();
}
