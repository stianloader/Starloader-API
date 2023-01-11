package de.geolykt.starloader.impl;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.Input.TextInputListener;

import de.geolykt.starloader.api.gui.InputDialog;

import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.TextInputDialogWidget;
import snoddasmannen.galimulator.ui.Widget;

public class StarloaderInputDialog extends TextInputDialogWidget implements InputDialog {

    /**
     * Reference to the implementation of the {@link TextInputListener} interface used by this dialog instance.
     */
    protected final TextInputWrapper wrapper;
    private boolean closed = false;

    public StarloaderInputDialog(String title, TextInputWrapper wrapper, String text, String hint) {
        super(title, wrapper, text, hint);
        this.wrapper = wrapper;
    }

    @Override
    public void addHook(@NotNull Consumer<@Nullable String> hook) {
        this.wrapper.addHook(hook);
    }

    @Override
    public void close() {
        closed = true;
        wrapper.canceled();
        propagateMessageLocally(Widget.WIDGET_MESSAGE.WIDGET_CLOSED);
    }

    @Override
    public void confirm() {
        closed = true;
        wrapper.input(c);
        propagateMessageLocally(Widget.WIDGET_MESSAGE.WIDGET_CLOSED);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void propagateMessageLocally(WIDGET_MESSAGE wIDGET_MESSAGE) {
        if (wIDGET_MESSAGE == WIDGET_MESSAGE.WIDGET_CLOSED) {
            closed = true;
        }
        super.propagateMessageLocally(wIDGET_MESSAGE);
    }

    /**
     * Sets the text that is within the dialog.
     *
     * @param text The new prefilled text.
     */
    public void setText(@NotNull String text) {
        super.c = text;
    }

    @Override
    public void a(char character) { // TODO deobf
        if (character == '\r') {
            try {
                Space.getMainTickLoopLock().acquire(2);
                try {
                    super.a(character);
                } finally {
                    Space.getMainTickLoopLock().release(2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            super.a(character);
        }
    }
}
