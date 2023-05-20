package de.geolykt.starloader.impl.gui.screencomponents;

import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.api.gui.screen.TextScreenComponent;
import de.geolykt.starloader.api.gui.text.FormattedText;

/**
 * The default implementation of {@link TextScreenComponent}.
 *
 * @deprecated Alongside the fact that the screen API's days are numbered, the Text API has been deprecated for
 * removal, making this class nonsensical.
 */
@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class SLTextScreenComponent implements TextScreenComponent {

    protected FormattedText currentText;
    protected Screen parent;
    protected final Supplier<@NotNull FormattedText> textSupplier;

    public SLTextScreenComponent(@NotNull FormattedText text) {
        this.currentText = Objects.requireNonNull(text, "Text cannot be null.");
        this.textSupplier = null;
    }

    public SLTextScreenComponent(@NotNull Supplier<@NotNull FormattedText> textSupplier) {
        this.textSupplier = Objects.requireNonNull(textSupplier, "Tried to create object with null text supplier.");
    }

    @Override
    public int getHeight() {
        return Galimulator.getConfiguration().getMinimumComponentHeight();
    }

    @Override
    public @NotNull LineWrappingInfo getLineWrappingInfo() {
        return LineWrappingInfo.wrapSameType();
    }

    @Override
    public @NotNull Screen getParentScreen() {
        return NullUtils.requireNotNull(parent, "Component has no parent screen specified");
    }

    @Override
    public @NotNull FormattedText getText() {
        if (textSupplier == null) {
            return NullUtils.requireNotNull(currentText);
        } else {
            return Objects.requireNonNull(textSupplier.get(), "text supplier gave a null text object.");
        }
    }

    @Override
    public int getWidth() {
        return this.getParentScreen().getInnerWidth();
    }

    @Override
    public boolean isDynamic() {
        return textSupplier != null;
    }

    @Override
    public boolean isSameType(@NotNull ScreenComponent component) {
        return component instanceof SLTextScreenComponent;
    }

    @Override
    public void setText(@NotNull FormattedText text) {
        if (textSupplier != null) {
            throw new UnsupportedOperationException("Text is set dynamically and therefore this method is not applicable.");
        }
        this.currentText = text;
    }

    @Override
    public int renderAt(float x, float y, @NotNull Camera camera) {
        return Math.round(getText().renderTextAt(x, y, camera)); // casting to int always rounds down
    }
}
