package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import de.geolykt.starloader.api.gui.WidgetActionListener;

import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.bi;

/**
 * A simple wrapper around the dialog, a graphical component of Galimulator.
 */
public class BasicDialog implements de.geolykt.starloader.api.gui.BasicDialog {

    protected final bi dialog;

    /**
     * Creates and displays a dialog.
     *
     * @param title           The title of the dialog
     * @param description     The description (content/body) of the dialog
     * @param choices         The buttons of the dialog
     * @param closeListeners  The close listeners that are applied to the dialog
     * @param actionListeners The action listeners that are applied to the dialog
     * @param duration        The duration that the dialog should stay opened in
     *                        seconds
     * @param playSFX         True if the close sound should be used.
     */
    public BasicDialog(@NotNull String title, @NotNull String description, @Nullable List<@NotNull String> choices,
            @NotNull ArrayList<@NotNull BasicDialogCloseListener> closeListeners,
            @NotNull ArrayList<@NotNull WidgetActionListener> actionListeners, int duration, boolean playSFX) {
        dialog = Space.a(title, description, choices, duration, null, true);
        dialog.a(new DialogCloseListenerWrapper(closeListeners, playSFX));
        dialog.a(new WidgetActionListenerWrapper(this, closeListeners, actionListeners));
    }

    /**
     * Obtains the time at which the dialog will be closed automatically, or -1 if
     * automatic closing is not done. The time is relative to the starting point of
     * {@link System#currentTimeMillis()} and is in milliseconds.
     */
    public long getAutocloseTime() {
        return dialog.d;
    }

    @Override
    public void close(@Nullable Object buttonPressed) {
        dialog.a(buttonPressed);
    }
}
