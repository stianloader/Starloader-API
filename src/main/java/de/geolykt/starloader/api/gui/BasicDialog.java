package de.geolykt.starloader.api.gui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.impl.DialogCloseListenerWrapper;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.bh;

/**
 * A simple wrapper around the dialog, a graphical component of Galimulator
 */
public class BasicDialog {

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
        bh dialog = Space.a(title, description, choices, duration, null, true);
        dialog.a(new DialogCloseListenerWrapper(listeners, playSFX));
    }
}
