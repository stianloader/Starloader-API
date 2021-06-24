package de.geolykt.starloader.api.gui;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InputDialog extends Closable {

    /**
     * Adds a hook to the input dialog. The hook will be called if the dialog is
     * closed; Where as the first parameter will be null if the dialog closed due to
     * the dialog input getting cancelled and a non-null value if it is not
     * cancelled, the value of the argument is the text that was just typed in.
     *
     * @param hook The Hook to attach
     */
    public void addHook(@NotNull Consumer<@Nullable String> hook);

    /**
     * Closes the object from view. However unlike {@link #close()} this method does
     * not discard data.
     *
     * @see #close()
     */
    public void confirm();
}
