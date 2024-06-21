package de.geolykt.starloader.api.gui;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dialog displayed to the user which allows the user to input arbitrary text.
 *
 * <p>Note that due to limitations with the underlying game engine, the user is likely to be incapable
 * of inputting quite a few special characters, especially with characters not present on US keyboards.
 * For examples, this includes the umlauts like '&auml;' or '&uuml;'.
 *
 * <p>Instances of this interface can be created using {@link TextInputBuilder}, which in turn can be
 * created via {@link Drawing#textInputBuilder(String, String, String)}. This interface should not be
 * implemented by mods.
 */
public interface InputDialog extends Closable {

    /**
     * Adds a hook to the input dialog. The hook will be called if the dialog is
     * closed; Where as the first parameter will be null if the dialog closed due to
     * the dialog input getting cancelled and a non-null value if it is not
     * cancelled, the value of the argument is the text that was just typed in.
     *
     * @param hook The Hook to attach
     */
    public void addHook(@NotNull Consumer<@Nullable String> hook);

    /**
     * Closes the object from view. However unlike {@link #close()} this method does
     * not discard data.
     *
     * @see #close()
     */
    public void confirm();
}
