package de.geolykt.starloader.api.serial.references;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Dateable;

/**
 * A persistent empire reference object that is intended to be (de-)serializable.
 *
 * <p>While an {@link Empire} instance is not stored if the underlying empire ceases to exist,
 * and thus cannot be used to reference an empire.
 *
 * <p>The main intended usecase of this class if an empire's name, color, or UID need
 * to be available persistently across saves. This includes graphing applications or if
 * genealogies or historical ownership need to be visualised.
 *
 * <p>This object represents a <b>hard</b> reference on an empire if it still exists in
 * reachable memory!
 *
 * <p>The {@link #equals(Object)} method of this class is only based on the empire's UID.
 * All other fields are ignored.
 *
 * @since 2.0.0-a20251224
 */
@AvailableSince("2.0.0-a20251224")
public final class PersistentEmpireReference implements Identifiable, Dateable {

    private boolean allowFetch;
    private final int deathYear;
    @Nullable
    private Empire empireInstance;
    private final int foundationYear;
    @NotNull
    private final Color gdxColor;
    @NotNull
    private final String name;
    private final int uid;

    /**
     * Creates a new {@link PersistentEmpireReference} instance using an {@link Empire} instance
     * as the initial link. This instance will use the underlying {@link Empire} reference for as long
     * as the savegame is not being reloaded, or in other words, for as long as the {@link Empire} object
     * lives in reachable memory.
     *
     * @param reference The underlying {@link Empire} reference.
     * @since 2.0.0-a20251224
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251224")
    public PersistentEmpireReference(@NotNull Empire reference) {
        this.empireInstance = reference;
        this.uid = reference.getUID();
        this.foundationYear = reference.getFoundationYear();
        this.name = reference.getEmpireName();
        this.gdxColor = reference.getGDXColor();
        this.deathYear = reference.getCollapseYear();
        this.allowFetch = false;
    }

    /**
     * Internal constructor used for {@link PersistentEmpireReference} de-serialisation.
     *
     * <p><b>This constructor is not public API. It may be removed without notice!</b>
     *
     * @param uid The empire's UID
     * @param foundationYear The foundation year
     * @param deathYear The collapse year
     * @param name The empire's name
     * @param gdxColor The empire's color
     * @since 2.0.0-a20251224
     */
    @Internal
    @AvailableSince("2.0.0-a20251224")
    public PersistentEmpireReference(int uid, int foundationYear, int deathYear, @NotNull String name, @NotNull Color gdxColor) {
        this.uid = uid;
        this.foundationYear = foundationYear;
        this.deathYear = deathYear;
        this.name = name;
        this.gdxColor = gdxColor;
        this.empireInstance = null;
        this.allowFetch  = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PersistentEmpireReference) {
            return ((PersistentEmpireReference) obj).uid == this.uid;
        }
        return false;
    }

    @Override
    @Contract(pure = true)
    public int getAge() {
        Empire e = this.getEmpire();
        if (e != null) {
            return e.getAge();
        } else {
            return this.deathYear - this.foundationYear;
        }
    }

    /**
     * The year the empire collapsed, or -1 if the empire did not collapse.
     *
     * <p>Internally, galimulator terms collapsed empires as being dead or not alive.
     * The term 'collapsed' as used by SLAPI is used for historical reasons and was coined
     * before in-depth knowledge of the game was present. However, both terms refer
     * to the same thing.
     *
     * @return the year of collapse
     * @since 2.0.0-a20251224
     */
    @Contract(pure = true)
    public int getCollapseYear() {
        Empire e = this.getEmpire();
        if (e != null) {
            return e.getCollapseYear();
        }
        return this.deathYear;
    }

    @Nullable
    private Empire getEmpire() {
        Empire e = this.empireInstance;

        if (e != null) {
            return e;
        } else if (this.allowFetch) {
            try {
                this.empireInstance = e = Galimulator.lookupEmpire(this.uid);
            } catch (IllegalArgumentException ignored) {}
            this.allowFetch = false;
            return e;
        }

        return null;
    }

    @Override
    @Contract(pure = true)
    public int getFoundationYear() {
        Empire e = this.getEmpire();
        if (e != null) {
            return e.getFoundationYear();
        } else {
            return this.foundationYear;
        }
    }

    /**
     * The empire color is used for multiple interfaces as well as to paint the
     * territory of an empire to a constant color, the color as such should not
     * change without reason to not confuse the user.
     *
     * @return The GDX {@link Color} assigned to the empire
     * @since 2.0.0-a20251224
     */
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251224")
    public Color getGdxColor() {
        Empire e = this.getEmpire();
        if (e != null) {
            return e.getGDXColor();
        } else {
            return this.gdxColor;
        }
    }

    /**
     * The name of the empire without any colors.
     *
     * @return A String that contains the empire's name
     * @since 2.0.0-a20251224
     */
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251224")
    public String getName() {
        Empire e = this.getEmpire();
        if (e != null) {
            return e.getEmpireName();
        } else {
            return this.name;
        }
    }

    @Override
    @Contract(pure = true)
    public int getUID() {
        return this.uid;
    }

    @Override
    public int hashCode() {
        return this.uid;
    }
}
