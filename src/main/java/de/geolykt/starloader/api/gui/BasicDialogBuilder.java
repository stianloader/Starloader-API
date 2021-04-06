package de.geolykt.starloader.api.gui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A builder that builds basic dialogs.
 */
public class BasicDialogBuilder {

    private String title;
    private String description;
    private List<String> choices;
    private ArrayList<BasicDialogCloseListener> closeListeners = new ArrayList<>();
    private ArrayList<WidgetActionListener> actionListeners = new ArrayList<>();
    private int duration = 0;
    private boolean playSFX = true;

    /**
     * Creates a DialogBuilder with the given title and description. The description
     * is often the main body of the dialog.
     *
     * @param title       The title of the dialog
     * @param description The description (main content) of the dialog
     */
    public BasicDialogBuilder(@NotNull String title, @NotNull String description) {
        this(title, description, null);
    }

    /**
     * Creates a DialogBuilder with the given title, description and choices. The
     * description is often the main body of the dialog. The choices are the
     * "Buttons" of the dialog box. If null, it will contain only a single "OK"
     * button.
     *
     * @param title       The title of the dialog
     * @param description The description (main content) of the dialog
     * @param choices     The choices of responses the user has, also known as the
     *                    buttons
     */
    public BasicDialogBuilder(@NotNull String title, @NotNull String description, @Nullable List<String> choices) {
        this.title = title;
        this.description = description;
        this.choices = choices;
    }

    /**
     * Sets the duration of the dialog in seconds. After the given amount of time
     * the dialog will auto-close without picking an option. A value of 0 disables
     * this.
     *
     * @param duration The time after the dialog closes in seconds
     * @return The instance of the builder
     */
    public BasicDialogBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Sets the choices of responses the user has, they are displayed as buttons. If
     * the list is null, then "OK" is assumed as the only choice. Please note that
     * the close button(s) are existing regardless.
     *
     * @param choices The text of the response buttons
     * @return The instance of the builder
     */
    public BasicDialogBuilder setChoices(@Nullable List<String> choices) {
        this.choices = choices;
        return this;
    }

    /**
     * @deprecated This method's name is ambiguous since there are now multiple
     *             types of listeners
     *
     *             Sets the close listeners for the Dialog. Any previous listeners
     *             are getting overridden
     *
     * @param listeners The list of listeners to use
     * @return The instance of the builder
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public BasicDialogBuilder setListeners(@NotNull ArrayList<BasicDialogCloseListener> listeners) {
        this.closeListeners = listeners;
        return this;
    }

    /**
     * @deprecated This method's name is ambiguous since there are now multiple
     *             types of listeners
     *
     *             Adds a close listener to the list of close listeners for the
     *             Dialog.
     *
     * @param closeListener The listener to add
     * @return The instance of the builder
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public BasicDialogBuilder addListener(@NotNull BasicDialogCloseListener closeListener) {
        closeListeners.add(closeListener);
        return this;
    }

    /**
     * Sets the close listeners for the Dialog. Any previous listeners are getting
     * overridden.
     *
     * @param listeners The list of listeners to use
     * @return The instance of the builder
     */
    public BasicDialogBuilder setCloseListeners(@NotNull ArrayList<BasicDialogCloseListener> listeners) {
        this.closeListeners = listeners;
        return this;
    }

    /**
     * Adds a close listener to the list of close listeners for the Dialog.
     *
     * @param closeListener The listener to add
     * @return The instance of the builder
     */
    public BasicDialogBuilder addCloseListener(@NotNull BasicDialogCloseListener closeListener) {
        closeListeners.add(closeListener);
        return this;
    }

    /**
     * Sets the widget actions listeners for the Dialog. Any previous listeners are
     * getting overridden.
     *
     * @param listeners The list of listeners to use
     * @return The instance of the builder
     */
    public BasicDialogBuilder setActionListeners(@NotNull ArrayList<WidgetActionListener> listeners) {
        this.actionListeners = listeners;
        return this;
    }

    /**
     * Adds a widget action listener to the list of close listeners for the Dialog.
     *
     * @param actionListener The listener to add
     * @return The instance of the builder
     */
    public BasicDialogBuilder addActionListener(@NotNull WidgetActionListener actionListener) {
        actionListeners.add(actionListener);
        return this;
    }

    /**
     * Enforces the behaviour of playing a sound to indicate that the selection has
     * been made.
     *
     * @return The instance of the builder
     */
    public BasicDialogBuilder playCloseSound() {
        playSFX = true;
        return this;
    }

    /**
     * Suppresses the behaviour of playing a sound to indicate that the selection
     * has been made. Without any listeners this will result in there being no audio
     * feedback when a selection has been made, which can be confusing to the user.
     *
     * @return The instance of the builder
     */
    public BasicDialogBuilder supressCloseSound() {
        playSFX = false;
        return this;
    }

    /**
     * Builds the dialog with the data within the Builder and displays it.
     *
     * @return The dialog that was built via the operation.
     */
    public BasicDialog buildAndShow() {
        return new de.geolykt.starloader.impl.BasicDialog(title, description, choices, closeListeners, actionListeners,
                duration, playSFX);
    }
}
