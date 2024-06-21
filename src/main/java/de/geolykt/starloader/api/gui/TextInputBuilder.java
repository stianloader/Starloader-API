package de.geolykt.starloader.api.gui;

import java.util.Collection;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.Input;

/**
 * Interface for building {@link InputDialog text input dialogs} which serve as a quick way of
 * obtaining user input.
 *
 * <p>Instances of this interface can be obtained using {@link Drawing#textInputBuilder(String, String, String)}.
 * This interface should not be implemented by mods.
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
    @NotNull
    public TextInputBuilder addHook(@NotNull Consumer<@Nullable String> hook);

    /**
     * Adds a multiple hook to the Dialog. These hooks will be called after the
     * Dialog is closed and user input is provided.
     *
     * @param hooks The hooks to add.
     * @return The builder instance, for chaining
     * @see #addHook(Consumer)
     * @see InputDialog#addHook(Consumer)
     */
    @NotNull
    public TextInputBuilder addHooks(@NotNull Collection<@NotNull Consumer<@Nullable String>> hooks);

    /**
     * Builds the dialog. Note that due to how the game is built, the return value
     * will always be null if native keyboard input is enabled. This may get changed
     * in the future, but that is the current implementation. However this does not
     * mean that the hooks will not be called; they will be called either way.
     *
     * <p>Note: As native text input using
     * {@link Input#getTextInput(com.badlogic.gdx.Input.TextInputListener, String, String, String)}
     * is not supported in the LWJGL3 backend of libGDX, future versions of SLAPI
     * which are designed to only run under LWJGL3 (this is planned to happen one day
     * or another) may explicitly disable the ability to make use of native text
     * input.
     *
     * @return The {@link InputDialog} that was just built, if applicable
     */
    @Nullable
    public InputDialog build();

    /**
     * Updates the hint of the Dialog.
     * The hint of the dialog, is - misleadingly - the text of the widget, not the prefilled text.
     * Hint and text only differ in colour.
     *
     * @param hint The hint of the dialog.
     * @return The builder instance, for chaining
     */
    @NotNull
    public TextInputBuilder setHint(@NotNull String hint);

    /**
     * Sets the initial text of the dialog. Unlike {@link #setText(String)}, this is the prefilled text - for real.
     *
     * @param text The text to use
     * @return The builder instance, for chaining
     */
    @NotNull
    public TextInputBuilder setInitialText(@NotNull String text);

    /**
     * Updates the text of the Dialog.
     * The text of the dialog, is - misleadingly - the text of the widget, not the prefilled text.
     * Hint and text only differ in colour.
     *
     * @param text The text of the dialog.
     * @return The builder instance, for chaining
     */
    @NotNull
    public TextInputBuilder setText(@NotNull String text);

    /**
     * Updates the title of the Dialog.
     *
     * @param title The title of the dialog.
     * @return The builder instance, for chaining
     */
    @NotNull
    public TextInputBuilder setTitle(@NotNull String title);
}
