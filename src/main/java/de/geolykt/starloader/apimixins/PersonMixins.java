package de.geolykt.starloader.apimixins;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.people.DynastyMember;

import snoddasmannen.galimulator.Job;
import snoddasmannen.galimulator.Person;

/**
 * Mixins targeting the Person class.
 * Used for implementing the {@link DynastyMember} interface on it.
 */
@Mixin(Person.class)
public class PersonMixins implements DynastyMember {

    @Shadow
    private boolean alive;

    @Shadow
    private int birthMilliYear;

    @Shadow
    private String causeOfDeath;

    @Shadow
    private int childCount;

    @Shadow
    private Vector<Person.CVEntry> cv;

    @Shadow
    private boolean isFollowed;

    @Shadow
    private Job job;

    @Shadow
    private float prestige;

    /**
     * Simple utility function to not have to write the same code a lot again.
     *
     * @return Basically the current instance type-casted to Person.
     */
    @Unique
    @NotNull
    private final Person asGalimulatorPerson() {
        return (Person) (Object) this;
    }

    @Override
    public int getChildrenCount() {
        return this.childCount;
    }

    @Override
    @Nullable
    public String getDeathReason() {
        return this.causeOfDeath;
    }

    @Override
    public int getDeathYear() {
        return this.cv.get(0).endTime;
    }

    @Override
    @Nullable
    @Deprecated
    public de.geolykt.starloader.api.empire.@Nullable ActiveEmpire getEmpire() {
        return (de.geolykt.starloader.api.empire.ActiveEmpire) this.getResidenceEmpire();
    }

    @Override
    public int getFoundationYear() {
        return this.birthMilliYear;
    }

    @Override
    public @NotNull String getFullName() {
        return NullUtils.requireNotNull(this.asGalimulatorPerson().q());
    }

    @Override
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public float getPrestige() {
        return this.prestige;
    }

    @Override
    @Nullable
    public Empire getResidenceEmpire() {
        if (this.alive) {
            if (this.job.getEmployer() == null) {
                return null; // this does not make much sense ... why would a job have no employer? But I am sure I coded that check for a reason.
            }
            return (Empire) this.job.getEmployer().getJobEmpire();
        } else {
            return null;
        }
    }

    @Override
    public int getUID() {
        return this.asGalimulatorPerson().id;
    }

    @Override
    public boolean hasDied() {
        return !this.alive;
    }

    @Override
    public boolean isFollowed() {
        return this.isFollowed;
    }

    @Override
    public void setFollowed(boolean followed) {
        this.isFollowed = followed;
    }
}
