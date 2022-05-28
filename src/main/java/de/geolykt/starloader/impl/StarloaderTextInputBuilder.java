package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.Collection;
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

    private @NotNull String hint;
    private final @NotNull List<Consumer<@Nullable String>> hooks = new ArrayList<>();
    private @NotNull String text;
    private @Nullable String initialText;
    private @NotNull String title;

    public StarloaderTextInputBuilder(@NotNull String title, @NotNull String text, @NotNull String hint) {
        this.title = title;
        this.text = text;
        this.hint = hint;
    }

    @Override
    public @NotNull TextInputBuilder addHook(@NotNull Consumer<@Nullable String> hook) {
        hooks.add(hook);
        return this;
    }

    @Override
    public @NotNull TextInputBuilder addHooks(@NotNull Collection<Consumer<@Nullable String>> hooks) {
        this.hooks.addAll(hooks);
        return this;
    }

    @Override
    @Nullable
    public InputDialog build() {
        if (Settings.EnumSettings.USE_NATIVE_KEYBOARD.b() == Boolean.TRUE) {
            Gdx.input.getTextInput(new TextInputWrapper(hooks), title, text, hint);
            return null;
        }
        // Based on the galactic preview and a few others, might require something better
        StarloaderInputDialog dialog = new StarloaderInputDialog(title, new TextInputWrapper(hooks), text, hint);
        String initialText = this.initialText;
        if (initialText != null) {
            dialog.setText(initialText);
        }
        // Basically Space#showWidget, but does not close non-persistent widgets
        Space.openedWidgets.add(new BufferedWidgetWrapper(dialog, GalFX.getScreenWidth() - dialog.getWidth() - 120.0f, 0.0, true, Widget.WIDGET_ALIGNMENT.MIDDLE));
        return dialog;
    }

    @Override
    public @NotNull TextInputBuilder setHint(@NotNull String hint) {
        this.hint = hint;
        return this;
    }

    @Override
    public @NotNull TextInputBuilder setText(@NotNull String text) {
        this.text = text;
        return this;
    }

    @Override
    public @NotNull TextInputBuilder setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull TextInputBuilder setInitialText(@NotNull String text) {
        this.initialText = text;
        return this;
    }
}
