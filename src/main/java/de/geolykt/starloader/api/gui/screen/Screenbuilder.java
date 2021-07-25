package de.geolykt.starloader.api.gui.screen;

import java.awt.Color;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.text.TextColor;

/**
 * Builder class to create {@link Screen} objects.
 */
public abstract class Screenbuilder {

    private static ComponentCreator componentCreator;

    /**
     * The implementation of {@link #getBuilder()} that is set via {@link #setFactory(Supplier)}.
     */
    private static Supplier<Screenbuilder> factory;

    /**
     * Obtains a <b>new</b> builder instance.
     * Under some circumstances - like loading this class before the API does - this will
     * return a {@link NullPointerException}. However in most instances, this can be ignored as
     * this will not happen.
     *
     * @return The newly created builder instance.
     */
    public static @NotNull Screenbuilder getBuilder() {
        return NullUtils.requireNotNull(factory.get());
    }

    /**
     * Obtains the currently valid component creator.
     * This component creator can be used to in turn to populate your newly created screens.
     *
     * @return The curently active {@link ComponentCreator}.
     * @throws NullPointerException If the component creator was not set.
     */
    public static @NotNull ComponentCreator getComponentCreator() {
        return NullUtils.requireNotNull(componentCreator);
    }

    /**
     * Sets the current {@link ComponentCreator} instance that should be returned by {@link #getComponentCreator()}.
     *
     * @param componentCreator The new valid ComponentCreator.
     */
    public static void setComponentCreator(@NotNull ComponentCreator componentCreator) {
        Screenbuilder.componentCreator = Objects.requireNonNull(componentCreator);
    }

    /**
     * Sets the implementation performed by {@link #getBuilder()}.
     *
     * @param factory The factory implementation
     */
    public static void setFactory(@NotNull Supplier<Screenbuilder> factory) {
        Screenbuilder.factory = NullUtils.requireNotNull(factory);
    }

    /**
     * Adds a callback-like functional interface to the screen.
     * This callback is called everytime the components within the screen need to be reordered or when
     * the screen has been otherwise been marked as dirty.
     *
     * @param provider The provider that does the things mentioned above
     */
    public abstract void addComponentProvider(@NotNull ComponentProvider provider);

    /**
     * Creates a {@link Screen} instance based on the current values of this builder.
     * Calling this method depeatedly should have no unexpected effects, though the
     * returned instance should always be newly created and may not be cached.
     * Requires {@link #setTitle(String)} to be invoked before this method as otherwise
     * it will throw an exception.
     * While not required, it is recommened to use {@link #addComponentProvider(ComponentProvider)}
     * beforehand as otherwise the screen will have no components to display. Additionally,
     * calling addComponentProvider(ComponentProvider) does not have any effects to
     * the newly created screen if the method was called after build().
     *
     * @return The newly created screen instance
     */
    public abstract @NotNull Screen build();

    /**
     * Sets the background color of the header.
     * All RGBA channels will be used and the default value is an Orage-ish color.
     *
     * @param awtColor The color to use as an Java AWT Color. It will get transformed into Galimulator's internal Color type later on.
     */
    public abstract void setHeaderColor(@NotNull Color awtColor);

    /**
     * Sets the background color of the header.
     * The default value is {@link TextColor#ORANGE}.
     *
     * @param textColor The color to use as an Starloader text TextColor. It will get transformed into Galimulator's internal Color type later on.
     */
    public abstract void setHeaderColor(@NotNull TextColor textColor);

    /**
     * Sets the title of the screen as shown in the screen header.
     * The default color of this title is white.
     * This method is a required operation and otherwise {@link #build()} will fail.
     *
     * @param title The title of the screen.
     */
    public abstract void setTitle(@NotNull String title);

    /**
     * Sets the fixed-sized width of the screen.
     * The screen height will be determined based on this width and the elements
     * within this screen.
     * Default is 450. I assume that the unit is pixels.
     *
     * @param width The width of the screen.
     */
    public abstract void setWidth(int width);

    /**
     * <b>The Starloader API will not make any guarantees on when the width will update.</b>
     *<br/>
     * Sets the width as a dynamically changeable function.
     * The screen height will be determined based on this width and the elements
     * within this screen. Calling this method with a non-null value automatically voids
     * any previous calls to {@link #setWidth(int)}. Calling it with a null value does the contrary.
     * I assume that the width is in pixels.
     *
     * @param width The width of the screen.
     */
    public abstract void setWidthProvider(@Nullable IntSupplier width);
}
