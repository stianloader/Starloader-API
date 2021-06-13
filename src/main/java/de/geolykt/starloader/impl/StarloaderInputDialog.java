package de.geolykt.starloader.impl;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.InputDialog;

import snoddasmannen.galimulator.ui.Widget;
import snoddasmannen.galimulator.ui.on;

public class StarloaderInputDialog extends on implements InputDialog {

    protected final TextInputWrapper wrapper;

    public StarloaderInputDialog(String title, TextInputWrapper wrapper, String text, String hint) {
        super(title, wrapper, text, hint);
        this.wrapper = wrapper;
    }

    @Override
    public void addHook(@NotNull Consumer<String> hook) {
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
}
