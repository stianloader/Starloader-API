package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.gui.AutocloseableDialog;
import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import de.geolykt.starloader.api.gui.DialogCloseCause;
import de.geolykt.starloader.api.gui.WidgetAction;
import de.geolykt.starloader.api.gui.WidgetActionListener;

import snoddasmannen.galimulator.ui.Widget;

public class WidgetActionListenerWrapper implements snoddasmannen.galimulator.hw {

    private final List<@NotNull BasicDialogCloseListener> closeListeners;
    private final List<@NotNull WidgetActionListener> actionListeners;
    private final AutocloseableDialog parent;

    /**
     * Initiates the wrapper with the parent and listeners known. The listeners can
     * be modified later however, provided the list implementation supports that.
     *
     * @param parent          The parent that is closable automatically, a null
     *                        value means that it will never be closed
     *                        automatically.
     * @param closeListeners  The close listeners to have initially
     * @param actionListeners The widget action listeners to have initially
     */
    WidgetActionListenerWrapper(@Nullable AutocloseableDialog parent,
            @NotNull List<@NotNull BasicDialogCloseListener> closeListeners,
            @NotNull List<@NotNull WidgetActionListener> actionListeners) {
        this.parent = parent;
        this.closeListeners = closeListeners;
        this.actionListeners = actionListeners;
    }

    /**
     * Initiates the wrapper with a parent that is closable automatically. If that
     * parameter is null it should behave the same way as
     * {@link #WidgetActionListenerWrapper()}
     *
     * @param parent The parent dialog
     */
    public WidgetActionListenerWrapper(@Nullable AutocloseableDialog parent) {
        this(parent, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Initiates the wrapper with no further information. If the parent Widget is an
     * automatically closing dialog, then
     * {@link #WidgetActionListenerWrapper(AutocloseableDialog)} should be preferred
     * as otherwise the {@link BasicDialogCloseListener} might get multiple
     * automatic close notifications.
     */
    public WidgetActionListenerWrapper() {
        this(null, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public void a(Widget.WIDGET_MESSAGE var1) {
        if (var1 == Widget.WIDGET_MESSAGE.WIDGET_CLOSED) {
            // Check if it was closed automatically due to timeout, if it was, then ignore
            // this request
            if (parent == null) {
                notifyClose(DialogCloseCause.MANUAL_CLOSE);
            } else {
                long timeout = parent.getAutocloseTime();
                if (timeout == -1 || timeout < System.currentTimeMillis()) {
                    notifyClose(DialogCloseCause.MANUAL_CLOSE);
                }
            }
            for (WidgetActionListener listener : actionListeners) {
                listener.onAction(WidgetAction.CLOSE);
            }
        } else {
            if (var1 == null) {
                DebugNagException.nag("Null widget message!");
            }
            WidgetAction action = var1 == Widget.WIDGET_MESSAGE.WIDGET_RESIZED ? WidgetAction.RESIZE : WidgetAction.REDRAW;
            for (WidgetActionListener listener : actionListeners) {
                listener.onAction(action);
            }
        }
    }

    private void notifyClose(@NotNull DialogCloseCause cause) {
        for (BasicDialogCloseListener listener : closeListeners) {
            listener.onClose(cause, null);
        }
    }

    public void addListener(@NotNull BasicDialogCloseListener listener) {
        closeListeners.add(listener);
    }

    public void addListener(@NotNull WidgetActionListener listener) {
        actionListeners.add(listener);
    }
}
