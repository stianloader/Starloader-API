package de.geolykt.starloader.api.gui.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.impl.gui.screencomponents.SLScreenComponent;

/**
 * A basic component provider interface that allows to dynamically create objects.
 * This is currently only used in the {@link ScreenBuilder} class, however
 * this interface might see more widespread use if noone comes with a better alternative,
 * because right now this interface is pretty hacky.
 * However I totally didn't create this method because we would otherwise come into unbelievable
 * terrains of {@link NotNull}
 *
 * @since 1.4.0
 * @deprecated This interface contains a serious design flaw that made this interface almost unusable.
 * This flaw was addressed in {@link ComponentSupplier}, which replaces this interface.
 */
@FunctionalInterface
@Deprecated(forRemoval = true, since = "1.5.0")
public interface ComponentProvider extends ComponentSupplier {

    /**
     * The parameter of this method is the current list of components that would need to be displayed
     * by the screen. The list is modifiable and the {@link List#add(Object)} method can be
     * used to add components to the screen.
     *
     * @param existingComponents The currently existing components
     * @deprecated The instance of the screen is not passed alongside this method, which makes creating
     * new (custom) components near to impossible without relying on the internal {@link SLScreenComponent} interface.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void supplyComponent(@NotNull List<@NotNull ScreenComponent> existingComponents);

    /**
     * {@inheritDoc}
     */
    @Override
    public default void supplyComponent(@NotNull Screen screen, @NotNull List<@NotNull ScreenComponent> existingComponents) {
        supplyComponent(existingComponents);
    }
}
