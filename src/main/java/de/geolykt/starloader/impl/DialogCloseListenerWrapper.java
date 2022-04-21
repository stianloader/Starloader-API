package de.geolykt.starloader.impl;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import de.geolykt.starloader.api.gui.DialogCloseCause;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.ui.interface_0;

// TODO interface_0 = OptionChooseListener
public class DialogCloseListenerWrapper implements interface_0 {

    private final ArrayList<@NotNull BasicDialogCloseListener> listeners;
    private boolean playSFX = true;

    public DialogCloseListenerWrapper() {
        listeners = new ArrayList<>();
    }

    public DialogCloseListenerWrapper(@NotNull ArrayList<@NotNull BasicDialogCloseListener> listeners, boolean playCloseSound) {
        this.listeners = listeners;
        playSFX = playCloseSound;
    }

    @Override
    public void a(Object var1) {
        if (var1 == null) {
            for (BasicDialogCloseListener closeListener : listeners) {
                closeListener.onClose(DialogCloseCause.AUTOMATIC_CLOSE, null);
            }
        } else {
            for (BasicDialogCloseListener closeListener : listeners) {
                closeListener.onClose(DialogCloseCause.BUTTON_CLICK, var1.toString());
            }
        }
        if (playSFX) {
            AudioSampleWrapper.UI_SMALL_SELECT.play();
        }
    }

    public void addListener(@NotNull BasicDialogCloseListener listener) {
        listeners.add(listener);
    }

    public void doPlayCloseSound(boolean doPlayCloseSound) {
        playSFX = doPlayCloseSound;
    }
}
