package de.geolykt.starloader.api.utils;

import de.geolykt.starloader.api.Galimulator;

/**
 * The noise provider exposes internal algorithms to generate various forms of noises.
 * As of now the default provider may not be all too efficient but considering the fact that the
 * implementation can change at a moment's notice, it is still advisable to use this interface
 * instead of using another implementation unless this noise provider is not sufficient for the task
 * at hand. An instance of this interface can be obtained through {@link Galimulator#getNoiseProvider()}.
 *
 * @author Geolykt
 * @since 2.0.0
 */
public interface NoiseProvider {

    /**
     * Generate perlin noise based on a given noise and with the given ocate count.
     * The baseNoise array will not be altered and can be considered as a kind of seed.
     * Ideally every value in the baseNoise array is between 0.0 (inclusive) and 1.0 (exclusive).
     * The returned array will have the same dimensions as the "baseNoise" array.
     * Furthermore all values in the returned array will be between 0.0 and 1.0.
     *
     * @param baseNoise The base noise to use
     * @param octaveCount The amount of octaves to generate
     * @return The generated perlin noise
     * @since 2.0.0
     */
    public float[][] generatePerlinNoise(final float[][] baseNoise, int octaveCount);

    /**
     * Generates a two-dimensional array which is populated by random perlin noise.
     * For positive values of width and height,
     * {@code generatePerlinNoise(width, height, x)[width - 1][height - 1]} must be
     * a valid call.
     * Furthermore all values in the returned array will be between 0.0 and 1.0.
     *
     * @param width The width of the array
     * @param height The height of the array
     * @param octaveCount The amount of octaves to generate
     * @return The generated array
     * @since 2.0.0
     */
    public float[][] generatePerlinNoise(int width, int height, int octaveCount);

    /**
     * Generates a two-dimensional array which is populated by white noise.
     * For positive values of width and height,
     * {@code generateWhiteNoise(width, height)[width - 1][height - 1]} must be
     * a valid call.
     * All values will be between 0.0 (inclusive) and 1.0 (exclusive).
     *
     * @param width The width of the array
     * @param height The height of the array
     * @return The generated array
     * @since 2.0.0
     */
    public float[][] generateWhiteNoise(int width, int height);
}
