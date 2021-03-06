package de.geolykt.starloader.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.bh;

/**
 * A simple wrapper around the dialog, a graphical component of Galimulator
 */
public class BasicDialog implements de.geolykt.starloader.api.gui.BasicDialog {

    protected final bh dialog;
    protected final long closeTime;

    /**
     * Creates and displays a dialog
     * @param title The title of the dialog
     * @param description The description (content/body) of the dialog
     * @param choices The buttons of the dialog
     * @param listeners The listeners that are applied to the dialog
     * @param duration The duration that the dialog should stay opened in seconds
     * @param playSFX True if the close sound should be used.
     */
    public BasicDialog(@NotNull String title, @NotNull String description, @Nullable List<String> choices,
            @NotNull ArrayList<BasicDialogCloseListener> listeners, int duration, boolean playSFX) {
        dialog = Space.a(title, description, choices, duration, null, true);
        dialog.a(new DialogCloseListenerWrapper(listeners, playSFX));
        // Luckily the close time is final, so we only have to get it once
        try {
            Field field = this.dialog.getClass().getField("d");
            field.setAccessible(true);
            closeTime = field.getLong(this.dialog);
            field.setAccessible(false);
        } catch (ReflectiveOperationException | SecurityException e) {
            throw new RuntimeException("Something went wrong while performing the reflections for Audiosample wrapping", e);
        }
    }

    /**
     * Obtains the time at which the dialog will be closed automatically, or -1 if automatic closing is not done.
     * The time is relative to the starting point of {@link System#currentTimeMillis()} and is in milliseconds.
     */
    public long getAutocloseTime() {
        return closeTime;
    }

    @Override
    public void close(@Nullable Object buttonPressed) {
        dialog.a(buttonPressed);
    }
}
