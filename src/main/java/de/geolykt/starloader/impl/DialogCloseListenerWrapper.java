package de.geolykt.starloader.impl;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import de.geolykt.starloader.api.gui.DialogCloseCause;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.ui.OptionSelectionListener;

public class DialogCloseListenerWrapper implements OptionSelectionListener {

    private final ArrayList<@NotNull BasicDialogCloseListener> listeners;
    private boolean playSFX = true;

    public DialogCloseListenerWrapper() {
        this.listeners = new ArrayList<>();
    }

    public DialogCloseListenerWrapper(@NotNull ArrayList<@NotNull BasicDialogCloseListener> listeners, boolean playCloseSound) {
        this.listeners = listeners;
        this.playSFX = playCloseSound;
    }

    @Override
    public void a(Object var1) {
        if (var1 == null) {
            for (BasicDialogCloseListener closeListener : this.listeners) {
                closeListener.onClose(DialogCloseCause.AUTOMATIC_CLOSE, null);
            }
        } else {
            for (BasicDialogCloseListener closeListener : this.listeners) {
                closeListener.onClose(DialogCloseCause.BUTTON_CLICK, var1.toString());
            }
        }
        if (this.playSFX) {
            AudioSampleWrapper.UI_SMALL_SELECT.play();
        }
    }

    public void addListener(@NotNull BasicDialogCloseListener listener) {
        this.listeners.add(listener);
    }

    public void doPlayCloseSound(boolean doPlayCloseSound) {
        this.playSFX = doPlayCloseSound;
    }
}
