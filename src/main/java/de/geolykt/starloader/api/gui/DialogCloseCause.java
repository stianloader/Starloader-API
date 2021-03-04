package de.geolykt.starloader.api.gui;

public enum DialogCloseCause {
    /**
     * Called when no button was clicked and the timer ran out
     */
    AUTOMATIC_CLOSE,

    /**
     * Called when a button was clicked and as such the GUI closed
     */
    BUTTON_CLICK,

    /**
     * Called when the User closed the Dialog willingly. not yet implemented.
     */
    MANUAL_CLOSE;
}
