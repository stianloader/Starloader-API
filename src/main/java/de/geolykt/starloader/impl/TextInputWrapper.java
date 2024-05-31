package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.Input.TextInputListener;

public class TextInputWrapper implements TextInputListener {

    protected final List<Consumer<@Nullable String>> hooks;

    public TextInputWrapper(List<Consumer<@Nullable String>> hooks2) {
        this.hooks = hooks2;
    }

    public TextInputWrapper() {
        this.hooks = new ArrayList<>();
    }

    public void addHook(Consumer<@Nullable String> hook) {
        this.hooks.add(hook);
    }

    @Override
    public void canceled() {
        this.hooks.forEach(hook -> hook.accept(null));
    }

    @Override
    public void input(String arg0) {
        this.hooks.forEach(hook -> hook.accept(arg0));
    }
}
