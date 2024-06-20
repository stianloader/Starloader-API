package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import de.geolykt.starloader.api.gui.WidgetActionListener;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.impl.gui.SLOptionChooserWidget;

import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.BufferedWidgetWrapper;
import snoddasmannen.galimulator.ui.OptionChooserWidget;
import snoddasmannen.galimulator.ui.Widget;

/**
 * A simple wrapper around the dialog, a graphical component of Galimulator.
 */
public class BasicDialog implements de.geolykt.starloader.api.gui.BasicDialog {

    /**
     * The underlying dialog widget wrapped by this {@link BasicDialog} instance.
     *
     * @deprecated This field will likely be replaced with a {@link Canvas} in the future,
     * and the access will be reduced to 'private' at some point in time. This may even occur
     * before all other breaking changes are performed in the 3.0.0 release cycle as this
     * is internal API.
     */
    @Deprecated
    @DeprecatedSince("2.0.0-a20240620")
    @ScheduledForRemoval
    protected final OptionChooserWidget dialog;

    private boolean closed = false;

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
        this.dialog = new SLOptionChooserWidget(title, description, choices, duration, null);
        Space.openedWidgets.add(new BufferedWidgetWrapper(this.dialog, 0.0, 0.0, true, Widget.WIDGET_ALIGNMENT.MIDDLE));
        this.dialog.a((Widget.WIDGET_MESSAGE msg) -> {
            if (msg == Widget.WIDGET_MESSAGE.WIDGET_CLOSED) {
                this.closed = true;
            }
        });
        this.dialog.registerSelectionListener((Object obj) -> {
            this.closed = true;
        });
        this.dialog.registerSelectionListener(new DialogCloseListenerWrapper(closeListeners, playSFX));
        this.dialog.a(new WidgetActionListenerWrapper(this, closeListeners, actionListeners));
    }

    /**
     * Obtains the time at which the dialog will be closed automatically, or -1 if
     * automatic closing is not done. The time is relative to the starting point of
     * {@link System#currentTimeMillis()} and is in milliseconds.
     */
    @Override
    public long getAutocloseTime() {
        return this.dialog.d;
    }

    @Override
    public void close(@Nullable Object buttonPressed) {
        this.dialog.a(buttonPressed);
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }
}
