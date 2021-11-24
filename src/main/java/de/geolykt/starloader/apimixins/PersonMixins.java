package de.geolykt.starloader.apimixins;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.empire.ActiveEmpire;
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
    private final @NotNull Person asGalimulatorPerson() {
        return (Person) (Object) this;
    }

    @Override
    public int getChildrenCount() {
        return childCount;
    }

    @Override
    public @Nullable String getDeathReason() {
        return causeOfDeath;
    }

    @Override
    public int getDeathYear() {
        return this.cv.get(0).endTime;
    }

    @Override
    public @Nullable ActiveEmpire getEmpire() {
        if (alive) {
            if (job.getEmployer() == null) {
                return null; // this does not make much sense ... why would a job have no employer? But I am sure I coded that check for a reason.
            }
            return (@Nullable ActiveEmpire) job.getEmployer().getJobEmpire();
        } else {
            return null;
        }
    }

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @Override
    public @NotNull String getFullName() {
        return NullUtils.requireNotNull(asGalimulatorPerson().q());
    }

    @Override
    public float getPrestige() {
        return prestige;
    }

    @Override
    public int getUID() {
        return asGalimulatorPerson().id;
    }

    @Override
    public boolean hasDied() {
        return !alive;
    }

    @Override
    public boolean isFollowed() {
        return isFollowed;
    }

    @Override
    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
