package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.empire.EmpireAchievement;

@Mixin(snoddasmannen.galimulator.EmpireAchievement.class)
public class EmpireAchievementMixins implements EmpireAchievement {
    @Shadow
    private int timestamp;
    @Shadow
    private snoddasmannen.galimulator.EmpireAchievement.EmpireAchievementType type;

    @SuppressWarnings("null")
    @Override
    @NotNull
    public EmpireAchievementType getAchievement() {
        return (EmpireAchievementType) (Object) type;
    }

    @Override
    public int getObtainTime() {
        return this.timestamp;
    }
}
