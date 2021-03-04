package de.geolykt.starloader.impl;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.DialogCloseCause;
import de.geolykt.starloader.api.gui.BasicDialogCloseListener;
import snoddasmannen.galimulator.AudioManager$AudioSample;
import snoddasmannen.galimulator.ui.bk;

public class DialogCloseListenerWrapper implements bk {

    private final ArrayList<BasicDialogCloseListener> listeners;
    private boolean playSFX = true;

    public DialogCloseListenerWrapper() {
        listeners = new ArrayList<>();
    }

    public DialogCloseListenerWrapper(@NotNull ArrayList<BasicDialogCloseListener> listeners, boolean playCloseSound) {
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
            AudioManager$AudioSample.f.a();
        }
    }

    public void addListener(@NotNull BasicDialogCloseListener listener) {
        listeners.add(listener);
    }

    public void doPlayCloseSound(boolean doPlayCloseSound) {
        playSFX = doPlayCloseSound;
    }

}
