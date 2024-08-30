package de.geolykt.starloader.api.gui;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import net.minestom.server.extras.selfmodification.MinestomExtensionClassLoader;

/**
 * The sidebar is a series of buttons that are always shown in the right side of the GUI interface.
 * This class attempts to allow you to define your own implementations of it.
 */
public abstract class SidebarInjector {

    /**
     * The injection points of the custom buttons.
     * While this enum does specify the appropriate positioning of the buttons,
     * it does not specify the exact positioning compared to other custom buttons.
     */
    public static enum Orientation {

        /**
         * Buttons with this orientation will be injected at the bottom,
         * though they will be injected over the bottommost button, as otherwise it wouldn't look nicely.
         */
        BOTTOM,

        /**
         * Buttons with this orientation will be injected at the top,
         * though they will be injected under the topmost button, as otherwise it wouldn't look nicely.
         */
        TOP;
    }

    /**
     * The implementation of this class.
     * Most methods will be relayed to this instance.
     */
    private static SidebarInjector implementation;

    /**
     * Adds a button that performs the given action when it is clicked on.
     *
     * @param orientation Where to inject the button
     * @param textureName The path of the texture of the button. Must be relative to the `data` file.
     * @param action The action to run when the button is clicked
     */
    public static void addButton(@NotNull Orientation orientation, @NotNull String textureName, @NotNull Runnable action) {
        SidebarInjector.getImplementation().addButton0(Objects.requireNonNull(orientation), textureName, action);
    }

    /**
     * Obtains the current handle that is used for static methods in this class.
     *
     * @return The handle
     */
    @NotNull
    public static SidebarInjector getImplementation() {
        SidebarInjector impl = SidebarInjector.implementation;
        if (impl == null) {
            throw new IllegalStateException("Implementation not yet set.");
        }
        return impl;
    }

    /**
     * Sets the handle for static methods in this class.
     *
     * @param implementation The implementation of the methods that should be used in the future.
     */
    public static void setImplementation(@NotNull SidebarInjector implementation) {
        if (SidebarInjector.implementation != null) {
            throw new IllegalStateException("Implementation is already set.");
        }
        SidebarInjector.implementation = implementation;
    }

    /**
     * Adds a button that performs the given action when it is clicked on.
     *
     * @param orientation Where to inject the button
     * @param textureName The path of the texture of the button. Must be relative to the `data` file.
     * @param action The action to run when the button is clicked
     */
    protected abstract void addButton0(@NotNull Orientation orientation, @NotNull String textureName, @NotNull Runnable action);

    static {
        if (!(SidebarInjector.class.getClassLoader() instanceof MinestomExtensionClassLoader)) {
            LoggerFactory.getLogger(SidebarInjector.class).error("Class loaded by improper classloader: {}", SidebarInjector.class.getClassLoader());
        }
    }
}
