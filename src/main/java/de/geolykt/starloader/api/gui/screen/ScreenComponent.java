package de.geolykt.starloader.api.gui.screen;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Renderable;

/**
 * A graphical component that can be added as a child object of a implementation of the {@link Screen} interface.
 * Due to how galimulator's component hierarchy works, not everything that looks like a component can be used, though
 * the SLAPI will lead efforts into making those available via this interface.
 */
public interface ScreenComponent extends Renderable {

    /**
     * Obtains the expected height of this component.
     *
     * <p>Depending on the screen implementation, this method may be called very, very frequently.
     * To reduce issues with both content within the screen and performance, it is best to implement
     * this method by returning a final field.
     *
     * @return The height of the component
     */
    public int getHeight();

    /**
     * Obtains the line wrapping metadata information that is assigned to this screen.
     * It should be honoured by the screen implementation unless the screen implementation is
     * making use of galimulator's dialog API at which time this method will be ignored
     * due to technical limitations.
     *
     * @return The assigned {@link LineWrappingInfo}.
     */
    public @NotNull LineWrappingInfo getLineWrappingInfo();

    /**
     * Obtains the most parent screen that this component belongs to.
     * As a rule of thumb, this should return the parent screen of the parent
     * component, unless they are located on two different screens for whatever reason.
     *
     * @return The parent screen
     */
    public @NotNull Screen getParentScreen();

    /**
     * Obtains the expected width of this component.
     *
     * <p>Depending on the screen implementation, this method may be called very, very frequently.
     * To reduce issues with both content within the screen and performance, it is best to implement
     * this method by returning a final field.
     *
     * @return The width of the component
     */
    public int getWidth();

    /**
     * Returns false if the other screen component is of another type as this component, otherwise true.
     * This is used for the {@link LineWrappingInfo#isWrapDifferentType()} method among other display-related things.
     * The implementation of this method should follow following guidelines:
     * <ul>
     *  <li> It should be reflective: If this method is invoked on x with x as an argument, then it should return true.</li>
     *  <li> It should be symmetric: If this method is invoked on x with y as an argument, then it should return the same
     *  compared to when this method is invoked on y with x as an argument. </li>
     *  <li> It should be transitive: If x.isSameType(y) is true and x.isSameType(z) is true, then y.isSameType(z) should
     *  also be true.</li>
     *  <li> It should (mostly) be consistent: Unless internal properties of the component(s) change, repeated invocations of
     *  x.isSameType(y) should return the same return value. </li>
     *  <li> It should be unintrusive: Invocations to this method should not change the state of the component. </li>
     * </ul>
     *<br/>
     * While most often a simple instanceof suffices, this may not always be the case. The simplest example would be
     * multiple implementations of the {@link TextScreenComponent}. Additionally the symmetric and transitive properties
     * of this method may be hard to achieve if the component's implementations stem from two different source extensions,
     * as the implementations may not be aware of each other, while still implementing a similar type. Thus usage of this
     * method should be performed with a grain of salt.
     *
     * @param component The component to compare this component to
     * @return Whether this component is of a similar type to the other component
     */
    public boolean isSameType(@NotNull ScreenComponent component);
}
