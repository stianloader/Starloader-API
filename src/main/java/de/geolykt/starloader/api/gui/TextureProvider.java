package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.NinePatch;

public interface TextureProvider {

    /**
     * Obtains the ninepatch responsible for alternative window frames.
     * This ninepatch corresponds to the "window3.png" sprite.
     * This sprite has more rounded corners than the "standard" window frame,
     * which ironically is used less often than this ninepatch.
     *
     * @return The alternate window ninepatch
     */
    public @NotNull NinePatch getAlternateWindowNinepatch();

    /**
     * Obtains the ninepatch responsible for button background of more non-rounded rectangular buttons.
     * One place to find these buttons is in the menu for creating new dynasties.
     * This ninepatch corresponds to the "specialsbox.png" sprite.
     * Internally this ninepatch is called the NICEBUTTON ninepatch.
     *
     * @return The box button ninepatch
     */
    public @NotNull NinePatch getBoxButtonNinePatch();

    /**
     * Obtains the ninepatch responsible for button background of most buttons.
     * This ninepatch corresponds to the "button3.png" sprite.
     *
     * @return The button ninepatch
     */
    public @NotNull NinePatch getRoundedButtonNinePatch();

    /**
     * Obtains the ninepatch responsible for window frames.
     * This ninepatch corresponds to the "window.png" sprite.
     * This sprite looks more rectangular that the alternate window variant,
     * which ironically is used more often.
     *
     * @return The window ninepatch
     */
    public @NotNull NinePatch getWindowNinepatch();
}
