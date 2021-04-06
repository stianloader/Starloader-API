package de.geolykt.starloader.api.gui;

import java.util.Collection;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for building Text Input Dialogs who easily provide User input.
 */
public interface TextInputBuilder {

    /**
     * Adds a single hook to the Dialog. This hook will be called after the Dialog
     * is closed and user input is provided.
     *
     * @param hook The hook to add.
     * @return The builder instance, for chaining
     * @see TextInputBuilder#addHooks(Collection)
     * @see InputDialog#addHook(Consumer)
     */
    public @NotNull TextInputBuilder addHook(@NotNull Consumer<String> hook);

    /**
     * Adds a multiple hook to the Dialog. These hooks will be called after the
     * Dialog is closed and user input is provided.
     *
     * @param hooks The hooks to add.
     * @return The builder instance, for chaining
     * @see #addHook(Consumer)
     * @see InputDialog#addHook(Consumer)
     */
    public @NotNull TextInputBuilder addHooks(@NotNull Collection<Consumer<String>> hooks);

    /**
     * Builds the dialog. Note that due to how the Game is built, the return value
     * will always be null if native keyboard input is enabled. This may get changed
     * in the future, but that is the current implementation. However this does not
     * mean that the hooks will not be called; they will be called either way.
     *
     * @return The {@link InputDialog} that was just built, if applicable
     */
    public @Nullable InputDialog build();

    /**
     * Updates the hint of the Dialog.
     *
     * @param hint The hint of the dialog.
     * @return The builder instance, for chaining
     */
    public @NotNull TextInputBuilder setHint(@NotNull String hint);

    /**
     * Updates the text of the Dialog.
     *
     * @param text The text of the dialog.
     * @return The builder instance, for chaining
     */
    public @NotNull TextInputBuilder setText(@NotNull String text);

    /**
     * Updates the title of the Dialog.
     *
     * @param title The title of the dialog.
     * @return The builder instance, for chaining
     */
    public @NotNull TextInputBuilder setTitle(@NotNull String title);
}
