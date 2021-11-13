package de.geolykt.starloader.impl.registry;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.registry.RegistryExpander;

import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.GalColor;

/**
 * An implementation of {@link EmpireSpecial} that delegates method calls to field constants.
 * This class is used in combination to starloader's registry system.
 *
 * @see RegistryExpander#addEmpireSpecial(de.geolykt.starloader.api.NamespacedKey, String, String, String, String, Color, float, float, float, float, boolean)
 */
public class SLEmpireSpecial extends EmpireSpecial {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 5285253728749916918L;

    /**
     * Whether empires with this special are banned from joining alliances.
     */
    protected final boolean alliancesBanned;

    /**
     * The color of the special. Used for GUI-related tasks, more specifically
     * the color of the name of the special within the leaderboard as well as
     * the color of the box of the special within the empire screen.
     */
    protected final GalColor color;

    /**
     * The industrial impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact. The industry modifier changes the ship cap, along other things.
     */
    protected final float ind;

    /**
     * Exact use of this variable not fully explored.
     */
    protected final float peace;

    /**
     * The stability impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact.
     */
    protected final float stability;

    /**
     * The technological impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
     */
    protected final float tech;

    /**
     * The constructor. Instances of this class should be registered via the appropriate registry.
     *
     * @param enumName The name of the special as returned by {@link Enum#name()}. Convention is to have it in UPPERCASE_SNAKE_CASE
     * @param ordinal The integer ordinal of the enum instance as specified by {@link Enum#ordinal()}. Should be equal to the registration order.
     * @param name The user-friendly name of the special.
     * @param abbreviation The user-friendly abbreviation of the special. Usually 3 letters long.
     * @param description The description of the special.
     * @param color The color of the special. Used for the special boxes.
     * @param tech The technological impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
     * @param ind The industrial impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact. The industry modifier changes the ship cap, along other things.
     * @param stability The stability impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
     * @param peace Exact use not fully explored.
     * @param alliancesBanned Whether empires with this special are banned from joining alliances.
     */
    public SLEmpireSpecial(String enumName, int ordinal, String name, String abbreviation, String description,
            Color color, float tech, float ind, float stability, float peace, boolean alliancesBanned) {
        super(enumName, ordinal, name, abbreviation, description);
        this.color = new GalColor(color);
        this.tech = tech;
        this.ind = ind;
        this.stability = stability;
        this.peace = peace;
        this.alliancesBanned = alliancesBanned;
    }

    @Override
    public float a() {
        return tech;
    }

    @Override
    public float b() {
        return ind;
    }

    @Override
    public float c() {
        return stability;
    }

    @Override
    public float d() {
        return peace;
    }

    @Override
    public boolean e() {
        return alliancesBanned;
    }

    @Override
    public GalColor j() {
        return color;
    }
}
