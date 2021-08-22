package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.impl.gui.screencomponents.SLScreenComponent;

/**
 * A collection class that sets the parent screen of all the components. Note: in order for this to work the component
 * must implement {@link SLScreenComponent}.
 */
public class SimpleScreenChildList extends ArrayList<@NotNull ScreenComponent> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6470572605204534819L;

    /**
     * Reference to the screen that is used to set the parent screen of all components that are added to this screen.
     */
    protected final @NotNull Screen parentScreen;

    /**
     * Constructor.
     *
     * @param parentScreen The parent screen that is used to set the parent screen of all components that are added to this screen
     */
    public SimpleScreenChildList(@NotNull Screen parentScreen) {
        this.parentScreen = Objects.requireNonNull(parentScreen, "The parent screen cannot be null.");
    }

    @Override
    public void add(int index, @NotNull ScreenComponent element) {
        setParent(element);
        super.add(index, element);
    }

    @Override
    public boolean add(@NotNull ScreenComponent e) {
        setParent(e);
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends @NotNull ScreenComponent> c) {
        Objects.requireNonNull(c, "\"c\" cannot be null.").forEach(this::setParent);
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends @NotNull ScreenComponent> c) {
        Objects.requireNonNull(c, "\"c\" cannot be null.").forEach(this::setParent);
        return super.addAll(index, c);
    }

    /**
     * Attempts to set the parent if not already set or verifies that the already set parent of the element is
     * equal to the parent of this collection.
     *
     * @param element The component to perform the operation on
     */
    protected void setParent(@NotNull ScreenComponent element) {
        if (element instanceof SLScreenComponent) {
            if (((SLScreenComponent) element).hasParentScreen()) {
                if (element.getParentScreen() != parentScreen) {
                    throw new IllegalArgumentException("The parent screen of the component that was added is not the parent screen that it should be. (Did you add the component to two different screens?)");
                }
            } else {
                ((SLScreenComponent) element).setParentScreen(parentScreen);
            }
        } else if (Objects.requireNonNull(element, "This list cannot store null values.").getParentScreen() != parentScreen) {
            throw new IllegalArgumentException("The element that was added to the list does not have the expected parent screen. Tip: you can implement SLScreenComponent so this list is done automatically.");
        }
    }
}
