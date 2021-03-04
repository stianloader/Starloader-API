package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A callback that is called when a dialog is closing.
 */
@FunctionalInterface
public interface BasicDialogCloseListener {

    /**
     * Called on close.
     * @param cause The cause of the dialog closing
     * @param buttonText The text of the button was clicked, can be null if it is not applicable
     */
    public void onClose(@NotNull DialogCloseCause cause, @Nullable String buttonText);
}
