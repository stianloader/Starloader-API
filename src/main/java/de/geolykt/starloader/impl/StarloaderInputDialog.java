package de.geolykt.starloader.impl;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.InputDialog;

import snoddasmannen.galimulator.ui.nt;
import snoddasmannen.galimulator.ui.Widget$WIDGET_MESSAGE;

public class StarloaderInputDialog extends nt implements InputDialog {

    private static final Field MESSAGE;

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
        b(Widget$WIDGET_MESSAGE.a);
    }

    @Override
    public void confirm() {
        boolean b = !MESSAGE.canAccess(null) && MESSAGE.trySetAccessible();
        try {
            wrapper.input(MESSAGE.get(null).toString());
            if (b) {
                MESSAGE.setAccessible(false);
            }
        } catch (Exception e) {
            throw new RuntimeException("Fatal error while performing reflections", e);
        }
        b(Widget$WIDGET_MESSAGE.a);
    }

    static {
        try {
            MESSAGE = nt.class.getDeclaredField("c");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Fatal error while performing reflections", e);
        }
    }
}
