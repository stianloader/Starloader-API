package de.geolykt.starloader.api.gui.screen;

import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NullUtils;

/**
 * Builder class to create {@link Screen} objects.
 */
public abstract class ScreenBuilder {

    /**
     * The componentCreator instance used by {@link #getComponentCreator()}
     * and set by {@link #setComponentCreator(ComponentCreator)}.
     *
     * @deprecated The type of this field has been deprecated for removal.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
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
     * @deprecated The return type has been deprecated for removal.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public static @NotNull ComponentCreator getComponentCreator() {
        return NullUtils.requireNotNull(componentCreator);
    }

    /**
     * Sets the current {@link ComponentCreator} instance that should be returned by {@link #getComponentCreator()}.
     *
     * @param componentCreator The new valid ComponentCreator.
     * @deprecated The return type has been deprecated for removal.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
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
     * @param supplier The component supplier that does the things mentioned above
     * @return The instance of the screen builder.
     * @since 1.5.0
     */
    @NotNull
    public abstract ScreenBuilder addComponentSupplier(@NotNull ComponentSupplier supplier);

    /**
     * Creates a {@link Screen} instance based on the current values of this builder.
     * Calling this method repeatedly should have no unexpected effects, though the
     * returned instance should always be newly created and may not be cached.
     * Requires {@link #setTitle(String)} to be invoked before this method as otherwise
     * it will throw an exception.
     * While not required, it is recommend to use {@link #addComponentSupplier(ComponentSupplier)}
     * beforehand as otherwise the screen will have no components to display. Additionally,
     * calling addComponentSupplier(ComponentSupplier) does not have any effects to
     * the newly created screen if the method was called after build().
     *
     * @return The newly created screen instance
     */
    @NotNull
    public abstract Screen build();

    /**
     * Sets the background color of the header.
     * All RGBA channels will be used and the default value is {@link Color#ORANGE}.
     *
     * @param gdxColor The color to use as an GDX Color. It will get transformed into Galimulator's internal Color type later on.
     * @deprecated This method does not follow the proper builder pattern.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public void setHeaderColor(@NotNull Color gdxColor) {
        withHeaderColor(gdxColor);
    }

    /**
     * Enables or Disables the header. By default it is true.
     * Should this method be called with false as a parameter then
     * the screen will show in headless mode and calls to
     * {@link #setHeaderColor(Color)}, {@link #setHeaderColor(TextColor)} and
     * {@link #setTitle(String)} will be ignored, however will not throw an exception.
     *
     * @param enabled Whether to enable the header.
     * @deprecated This method does not follow the proper builder pattern.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public void setHeaderEnabled(boolean enabled) {
        withHeaderEnabled(enabled);
    }

    /**
     * Sets the title of the screen as shown in the screen header.
     * The default color of this title is white.
     * This method is a required operation and otherwise {@link #build()} will fail,
     * except if {@link #setHeaderEnabled(boolean)} was called with false as a parameter.
     *
     * @param title The title of the screen.
     * @deprecated This method does not follow the proper builder pattern.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public void setTitle(@NotNull String title) {
        withTitle(title);
    }

    /**
     * Sets the fixed-sized width of the screen.
     * The screen height will be determined based on this width and the elements
     * within this screen.
     * Default is 450. I assume that the unit is pixels.
     *
     * @param width The width of the screen.
     * @deprecated This method does not follow the proper builder pattern.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public void setWidth(int width) {
        withWidth(width);
    }

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
     * @deprecated This method does not follow the proper builder pattern.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public void setWidthProvider(@Nullable IntSupplier width) {
        withWidthProvider(width);
    }

    /**
     * Sets the background color of the header.
     * All RGBA channels will be used and the default value is {@link Color#ORANGE}.
     *
     * @param gdxColor The color to use as an GDX Color. It will get transformed into Galimulator's internal Color type later on.
     * @return The current instance of the screen builder
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public abstract ScreenBuilder withHeaderColor(@NotNull Color gdxColor);

    /**
     * Enables or Disables the header. By default it is true.
     * Should this method be called with false as a parameter then
     * the screen will show in headless mode and calls to
     * {@link #setHeaderColor(Color)}, {@link #setHeaderColor(TextColor)} and
     * {@link #setTitle(String)} will be ignored, however will not throw an exception.
     *
     * @param enabled Whether to enable the header.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public abstract ScreenBuilder withHeaderEnabled(boolean enabled);

    /**
     * Sets the title of the screen as shown in the screen header.
     * The default color of this title is white.
     * This method is a required operation and otherwise {@link #build()} will fail,
     * except if {@link #setHeaderEnabled(boolean)} was called with false as a parameter.
     *
     * @param title The title of the screen.
     * @return The current instance of the screen builder
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public abstract ScreenBuilder withTitle(@NotNull String title);

    /**
     * Sets the fixed-sized width of the screen.
     * The screen height will be determined based on this width and the elements
     * within this screen.
     * Default is 450. I assume that the unit is pixels.
     *
     * @param width The width of the screen.
     * @return The current instance of the screen builder
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public abstract ScreenBuilder withWidth(int width);

    /**
     * <b>The Starloader API will not make any guarantees on when the width will update.</b>
     *<br/>
     * Sets the width as a dynamically changeable function.
     * The screen height will be determined based on this width and the elements
     * within this screen. Calling this method with a non-null value automatically voids
     * any previous calls to {@link #setWidth(int)}. Calling it with a null value does the contrary.
     * I assume that the width is in pixels.
     *
     * @param width An {@link IntSupplier} whose returned value will be the the width of the screen.
     * @return The current instance of the screen builder
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public abstract ScreenBuilder withWidthProvider(@Nullable IntSupplier width);
}
