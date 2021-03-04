package de.geolykt.starloader.api.gui;

/**
 * Exception thrown when something attempts to modify an already opened Dialog
 */
public class DialogAlreadyOpenedException extends IllegalStateException {

    private static final long serialVersionUID = 8969918145814804219L;

    public DialogAlreadyOpenedException() {
        super("Cannot modify a dialog when it was already opened.");
    }
}
