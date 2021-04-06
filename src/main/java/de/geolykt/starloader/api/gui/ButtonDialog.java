package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.Nullable;

public interface ButtonDialog extends Closable {

    /**
     * This call simulates a button click. However this method can also be called if
     * the dialog was closed automatically.
     *
     * @param buttonPressed Some magic value that I don't fully understand or null,
     *                      if the dialog was closed automatically
     */
    public void close(@Nullable Object buttonPressed); // TODO understand the parameter

    @Override
    public default void close() {
        close(null);
    }
}
