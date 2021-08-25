package de.geolykt.starloader.impl.gui.screencomponents;

import java.util.Objects;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.api.gui.screen.TextScreenComponent;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.impl.gui.SLAbstractWidget;
import snoddasmannen.galimulator.b.class_s;
import snoddasmannen.galimulator.ui.Widget;

/**
 * The default implementation of {@link TextScreenComponent}.
 */
public class SLTextScreenComponent implements TextScreenComponent, SLScreenComponent, class_s {

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
    public Widget b(boolean boolean1) {
        return new SLAbstractWidget() {

            @Override
            public int getHeight() { // height
                return Galimulator.getConfiguration().getMinimumComponentHeight();
            }

            @Override
            public int getWidth() { // width (I assume)
                return getParentScreen().getInnerWidth();
            }

            @Override
            public void onRender() {
                // TODO galimulator is a lot more complicated than I expected with text drawing. Perhaps it is better to introduce even more drawing methods.
                Camera camera = this.getCamera();
                if (camera == null) {
                    camera = getParentScreen().getCamera();
                }
                getText().renderAt(4.0F, this.getHeight(), NullUtils.requireNotNull(camera, "The camera is null, strange isn't it? Not that I fully expect this to happen or anything. But it will be the future me to resolve this either way, so fuck it."));
            }
        };
    }

    @Override
    public String e() {
        return null; // Not entirely sure what this does, perhaps it is for categories?
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
    public boolean hasParentScreen() {
        return parent != null;
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
    public void setParentScreen(@NotNull Screen parent) {
        this.parent = Objects.requireNonNull(parent, "Tried to set parent screen to null.");
    }

    @Override
    public void setText(@NotNull FormattedText text) {
        if (textSupplier != null) {
            throw new UnsupportedOperationException("Text is set dynamically and therefore this method is not applicable.");
        }
        this.currentText = text;
    }
}
