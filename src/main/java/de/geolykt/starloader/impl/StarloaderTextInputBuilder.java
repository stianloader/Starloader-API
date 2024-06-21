package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.Gdx;

import de.geolykt.starloader.api.gui.InputDialog;
import de.geolykt.starloader.api.gui.TextInputBuilder;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Settings;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.BufferedWidgetWrapper;
import snoddasmannen.galimulator.ui.Widget;

public class StarloaderTextInputBuilder implements TextInputBuilder {

    @NotNull
    private String hint;
    @NotNull
    private final List<@NotNull Consumer<@Nullable String>> hooks = new ArrayList<>();
    @Nullable
    private String initialText;
    @NotNull
    private String text;
    @NotNull
    private String title;

    public StarloaderTextInputBuilder(@NotNull String title, @NotNull String text, @NotNull String hint) {
        this.title = title;
        this.text = text;
        this.hint = hint;
    }

    @Override
    @NotNull
    public TextInputBuilder addHook(@NotNull Consumer<@Nullable String> hook) {
        this.hooks.add(hook);
        return this;
    }

    @Override
    @NotNull
    public TextInputBuilder addHooks(@NotNull Collection<@NotNull Consumer<@Nullable String>> hooks) {
        this.hooks.addAll(hooks);
        return this;
    }

    @Override
    @Nullable
    public InputDialog build() {
        if (Settings.EnumSettings.USE_NATIVE_KEYBOARD.getValue() == Boolean.TRUE) {
            List<Consumer<@Nullable String>> surrogate = Collections.singletonList((s) -> {
                try {
                    Space.getMainTickLoopLock().acquire(2);
                    try {
                        this.hooks.forEach(c -> c.accept(s));
                    } finally {
                        Space.getMainTickLoopLock().release(2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Gdx.input.getTextInput(new TextInputWrapper(surrogate), this.title, this.text, this.hint);
            return null;
        }
        // Based on the galactic preview and a few others, might require something better
        StarloaderInputDialog dialog = new StarloaderInputDialog(this.title, new TextInputWrapper(this.hooks), this.text, this.hint);
        String initialText = this.initialText;
        if (initialText != null) {
            dialog.setText(initialText);
        }
        // Basically Space#showWidget, but does not close non-persistent widgets
        Space.openedWidgets.add(new BufferedWidgetWrapper(dialog, GalFX.getScreenWidth() - dialog.getWidth() - 120.0f, 0.0, true, Widget.WIDGET_ALIGNMENT.MIDDLE));
        return dialog;
    }

    @Override
    @NotNull
    public TextInputBuilder setHint(@NotNull String hint) {
        this.hint = hint;
        return this;
    }

    @Override
    @NotNull
    public TextInputBuilder setInitialText(@NotNull String text) {
        this.initialText = text;
        return this;
    }

    @Override
    @NotNull
    public TextInputBuilder setText(@NotNull String text) {
        this.text = text;
        return this;
    }

    @Override
    @NotNull
    public TextInputBuilder setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }
}
