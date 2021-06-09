package de.geolykt.starloader.api.empire;

/**
 * An enum representing the types that the planet can be.
 * This type determines the sprite of the planet.
 */
public enum PlanetType {
    /**
     * The planet mostly consists out of water.
     */
    AQUATIC,

    /**
     * A more exotic planet. THe surface will look more random.
     */
    EXOTIC,

    /**
     * A gas planet whose surface will consist of two or more colors.
     */
    GASEOUS_STRIPED,

    /**
     * A gaseous planet whose surface will mostly consist of a single color.
     */
    GASEOUS_UNICOLOR,

    /**
     * A planet with minimal atmosphere and survivability.
     * It is in a white-ish colour and the surface will mostly consist of rocks.
     */
    ROCKY,

    /**
     * The planet is earthlike.
     */
    TERRAN;
}
