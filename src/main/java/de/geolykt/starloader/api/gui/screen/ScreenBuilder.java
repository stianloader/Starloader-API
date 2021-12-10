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
public abstract class ScreenBuilder {

    /**
     * The componentCreator instance used by {@link #getComponentCreator()}
     * and set by {@link #setComponentCreator(ComponentCreator)}.
     */
    private static ComponentCreator componentCreator;

    /**
     * The implementation of {@link #getBuilder()} that is set via {@link #setFactory(Supplier)}.
     */
    private static Supplier<ScreenBuilder> factory;

    /**
     * Obtains a <b>new</b> builder instance.
     * Under some circumstances - like loading this class before the API does - this will
     * return a {@link NullPointerException}. However in most instances, this can be ignored as
     * this will not happen.
     *
     * @return The newly created builder instance.
     */
    public static @NotNull ScreenBuilder getBuilder() {
        return NullUtils.requireNotNull(factory.get());
    }

    /**
     * Obtains the currently valid component creator.
     * This component creator can be used to in turn to populate your newly created screens.
     *
     * @return The currently active {@link ComponentCreator}.
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
        ScreenBuilder.componentCreator = Objects.requireNonNull(componentCreator);
    }

    /**
     * Sets the implementation performed by {@link #getBuilder()}.
     *
     * @param factory The factory implementation
     */
    public static void setFactory(@NotNull Supplier<ScreenBuilder> factory) {
        ScreenBuilder.factory = NullUtils.requireNotNull(factory);
    }

    /**
     * Adds a callback-like functional interface to the screen.
     * This callback is called everytime the components within the screen need to be reordered or when
     * the screen has been otherwise been marked as dirty.
     *
     * @param provider The provider that does the things mentioned above
     * @since 1.4.0
     * @deprecated See the deprecation note of the {@link ComponentProvider} interface. Replaced by {@link #addComponentSupplier(ComponentSupplier)}.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void addComponentProvider(@NotNull ComponentProvider provider) {
        addComponentSupplier(provider);
    }

    /**
     * Adds a callback-like functional interface to the screen.
     * This callback is called everytime the components within the screen need to be reordered or when
     * the screen has been otherwise been marked as dirty.
     *
     * @param supplier The component supplier that does the things mentioned above
     * @return The instance of the screen builder.
     * @since 1.5.0
     */
    public abstract @NotNull ScreenBuilder addComponentSupplier(@NotNull ComponentSupplier supplier);

    /**
     * Creates a {@link Screen} instance based on the current values of this builder.
     * Calling this method repeatedly should have no unexpected effects, though the
     * returned instance should always be newly created and may not be cached.
     * Requires {@link #setTitle(String)} to be invoked before this method as otherwise
     * it will throw an exception.
     * While not required, it is recommend to use {@link #addComponentProvider(ComponentProvider)}
     * beforehand as otherwise the screen will have no components to display. Additionally,
     * calling addComponentProvider(ComponentProvider) does not have any effects to
     * the newly created screen if the method was called after build().
     *
     * @return The newly created screen instance
     */
    public abstract @NotNull Screen build();

    /**
     * Sets the background color of the header.
     * All RGBA channels will be used and the default value is an Orange-ish color.
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
     * Enables or Disables the header. By default it is true.
     * Should this method be called with false as a parameter then
     * the screen will show in headless mode and calls to
     * {@link #setHeaderColor(Color)}, {@link #setHeaderColor(TextColor)} and
     * {@link #setTitle(String)} will be ignored, however will not throw an exception.
     *
     * @param enabled Whether to enable the header.
     */
    public abstract void setHeaderEnabled(boolean enabled);

    /**
     * Sets the title of the screen as shown in the screen header.
     * The default color of this title is white.
     * This method is a required operation and otherwise {@link #build()} will fail,
     * except if {@link #setHeaderEnabled(boolean)} was called with false as a parameter.
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
