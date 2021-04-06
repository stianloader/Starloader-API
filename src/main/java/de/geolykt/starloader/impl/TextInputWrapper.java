package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.Input.TextInputListener;

public class TextInputWrapper implements TextInputListener {

    protected final List<Consumer<String>> hooks;

    public TextInputWrapper(List<Consumer<String>> hooks2) {
        hooks = hooks2;
    }

    public TextInputWrapper() {
        hooks = new ArrayList<>();
    }

    public void addHook(Consumer<String> hook) {
        hooks.add(hook);
    }

    @Override
    public void canceled() {
        hooks.forEach(hook -> hook.accept(null));
    }

    @Override
    public void input(String arg0) {
        hooks.forEach(hook -> hook.accept(arg0));
    }
}
