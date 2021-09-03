package de.geolykt.starloader.impl;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.Input.TextInputListener;

import de.geolykt.starloader.api.gui.InputDialog;

import snoddasmannen.galimulator.ui.Widget;
import snoddasmannen.galimulator.ui.on;

public class StarloaderInputDialog extends on implements InputDialog {

    /**
     * Reference to the implementation of the {@link TextInputListener} interface used by this dialog instance.
     */
    protected final TextInputWrapper wrapper;

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
        wrapper.canceled();
        b(Widget.WIDGET_MESSAGE.WIDGET_CLOSED);
    }

    @Override
    public void confirm() {
        wrapper.input(c);
        b(Widget.WIDGET_MESSAGE.WIDGET_CLOSED);
    }

    /**
     * Sets the text that is within the dialog.
     *
     * @param text The new prefilled text.
     */
    public void setText(@NotNull String text) {
        super.c = text;
    }
}
