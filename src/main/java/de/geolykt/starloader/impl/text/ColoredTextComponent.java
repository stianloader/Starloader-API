package de.geolykt.starloader.impl.text;

import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.text.TextComponent;

@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class ColoredTextComponent implements TextComponent {

    @NotNull
    protected final Color color;
    protected final Drawing.@NotNull TextSize size;
    @NotNull
    protected final String text;

    public ColoredTextComponent(@NotNull String s) {
        this(s, Objects.requireNonNull(Color.WHITE), Drawing.TextSize.SMALL);
    }

    public ColoredTextComponent(@NotNull String s, @NotNull Color color) {
        this(s, color, Drawing.TextSize.SMALL);
    }

    public ColoredTextComponent(@NotNull String s, @NotNull Color color, Drawing.@NotNull TextSize size) {
        this.text = s;
        this.color = color;
        this.size = Objects.requireNonNull(size);
    }

    @Override
    @NotNull
    public String getText() {
        return this.text;
    }

    @Override
    public float renderText(float x, float y) {
        return Drawing.drawText(this.text, x, y, this.color, this.size);
    }

    @Override
    public float renderTextAt(float x, float y, @NotNull Camera camera) {
        return Drawing.drawText(this.text, x, y, this.color, this.size, camera);
    }
}
