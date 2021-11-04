package de.geolykt.starloader.api.gui.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * A basic component provider interface that allows to dynamically create objects.
 * Unlike {@link ComponentProvider} this interface also passes the instance of the instance
 * of the screen that it is being used on, which makes this interface much more viable.
 *
 * @since 1.5.0
 */
public interface ComponentSupplier {

    /**
     * The parameter of this method is the current list of components that would need to be displayed
     * by the screen. The list is modifiable and the {@link List#add(Object)} method can be
     * used to add components to the screen.
     *
     * @param existingComponents The currently existing components
     */
    public void supplyComponent(@NotNull Screen screen, @NotNull List<@NotNull ScreenComponent> existingComponents);
}
