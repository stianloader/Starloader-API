package de.geolykt.starloader.impl.util;

import de.geolykt.starloader.api.utils.NoiseProvider;

import snoddasmannen.galimulator.PerlinNoiseGenerator;

/**
 * The default implementation of the {@link NoiseProvider} interface that delegates most calls to
 * galimulator classes. In the future a more sophisticated approach might be used.
 *
 * @since 2.0.0
 */
public class SLNoiseProvider implements NoiseProvider {

    @Override
    public float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount) {
        return PerlinNoiseGenerator.generatePerlinNoise(baseNoise, octaveCount);
    }

    @Override
    public float[][] generatePerlinNoise(int width, int height, int octaveCount) {
        return PerlinNoiseGenerator.generatePerlinNoise(width, height, octaveCount);
    }

    @Override
    public float[][] generateWhiteNoise(int width, int height) {
        return PerlinNoiseGenerator.generateWhiteNoise(width, height);
    }
}
