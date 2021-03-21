package de.geolykt.starloader.impl.text;

import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.NotNull;

import snoddasmannen.galimulator.GalColor;

public class JitterTextComponent extends ColoredTextComponent {

    protected final float intensity;

    public JitterTextComponent(@NotNull String text, @NotNull GalColor color, double intensity) {
        super(text, color);
        this.intensity = (float) intensity;
    }

    @Override
    public float renderText(float x, float y) {
        float jitterX = (ThreadLocalRandom.current().nextFloat() - 0.5f) * intensity;
        float jitterY = (ThreadLocalRandom.current().nextFloat() - 0.5f) * intensity;
        return super.renderText(jitterX + x, jitterY + y);
    }
}
