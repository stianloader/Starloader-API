package de.geolykt.starloader.api.gui;

/**
 * @deprecated This class is unused due to changes within the structure of the Dialog classes
 *
 * Exception thrown when something attempts to modify an already opened Dialog.
 */
@Deprecated(forRemoval = true, since = "1.3.0")
public class DialogAlreadyOpenedException extends IllegalStateException {

    private static final long serialVersionUID = 8969918145814804219L;

    public DialogAlreadyOpenedException() {
        super("Cannot modify a dialog when it was already opened.");
    }
}
