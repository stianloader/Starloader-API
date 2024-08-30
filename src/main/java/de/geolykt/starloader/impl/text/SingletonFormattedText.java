package de.geolykt.starloader.impl.text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;

@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class SingletonFormattedText implements FormattedText {

    protected final @NotNull FormattedTextComponent component;

    public SingletonFormattedText(@NotNull FormattedTextComponent component) {
        this.component = component;
    }

    public SingletonFormattedText(@NotNull String s) {
        this(new SingletonTextComponent(s));
    }

    public SingletonFormattedText(@NotNull String s, @NotNull Color color) {
        this(new SingletonTextComponent(s, color));
    }

    @Override
    @NotNull
    public List<@NotNull FormattedTextComponent> getComponents() {
        return Objects.requireNonNull(Arrays.asList(component));
    }

    @Override
    public @NotNull String getText() {
        return component.getText();
    }

    @Override
    public float renderTextAt(float x, float y, @NotNull Camera camera) {
        return component.renderTextAt(x, y, camera);
    }
}
