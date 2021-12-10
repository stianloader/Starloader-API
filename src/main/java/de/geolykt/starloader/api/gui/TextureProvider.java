package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface TextureProvider {

    /**
     * Obtains the texture region that is resolved by the given name.
     * Should there be no texture region by the given name (yet), then the texture
     * region for flower.png is returned. It will also return that texture region for an
     * empty name.
     *
     * @param name The name of the texture region. Usually the name of a file within the sprites folder.
     * @return The texture region registered under the name, or the texture region of flower.png
     */
    @NotNull
    public TextureRegion findTextureRegion(@NotNull String name);

    /**
     * Obtains the ninepatch responsible for alternative window frames.
     * This ninepatch corresponds to the "window3.png" sprite.
     * This sprite has more rounded corners than the "standard" window frame,
     * which ironically is used less often than this ninepatch.
     *
     * @return The alternate window ninepatch
     */
    @NotNull
    public NinePatch getAlternateWindowNinepatch();

    /**
     * Obtains the ninepatch responsible for button background of more non-rounded rectangular buttons.
     * One place to find these buttons is in the menu for creating new dynasties.
     * This ninepatch corresponds to the "specialsbox.png" sprite.
     * Internally this ninepatch is called the NICEBUTTON ninepatch.
     *
     * @return The box button ninepatch
     */
    @NotNull
    public NinePatch getBoxButtonNinePatch();

    /**
     * Obtains the ninepatch responsible for button background of most buttons.
     * This ninepatch corresponds to the "button3.png" sprite.
     *
     * @return The button ninepatch
     */
    @NotNull
    public NinePatch getRoundedButtonNinePatch();

    /**
     * Obtains the ninepatch responsible for window frames.
     * This ninepatch corresponds to the "window.png" sprite.
     * This sprite looks more rectangular that the alternate window variant,
     * which ironically is used more often.
     *
     * @return The window ninepatch
     */
    @NotNull
    public NinePatch getWindowNinepatch();
}
