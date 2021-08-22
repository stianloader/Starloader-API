package de.geolykt.starloader.api.gui.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * A basic component provider interface that allows to dynamically create objects.
 * This is currently only used in the {@link ScreenBuilder} class, however
 * this interface might see more widespread use if noone comes with a better alternative,
 * because right now this interface is pretty hacky.
 * However I totally didn't create this method because we would otherwise come into unbelievable
 * terrains of {@link NotNull}
 */
@FunctionalInterface
public interface ComponentProvider {

    /**
     * The parameter of this method is the current list of components that would need to be displayed
     * by the screen. The list is modifiable and the {@link List#add(Object)} method can be
     * used to add components to the screen.
     *
     * @param existingComponents The currently existing components
     */
    public void supplyComponent(@NotNull List<@NotNull ScreenComponent> existingComponents);
}
