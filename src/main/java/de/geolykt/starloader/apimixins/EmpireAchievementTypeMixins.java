package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.EmpireAchievement;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.GalColor;

@Mixin(snoddasmannen.galimulator.EmpireAchievement.EmpireAchievementType.class)
public class EmpireAchievementTypeMixins implements EmpireAchievement.EmpireAchievementType {

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the empire achievements registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initEmpireAchievements();
    }

    @Shadow
    private GalColor color;

    @Shadow
    private String description;

    @Unique
    @Nullable
    private NamespacedKey registryKey = null;

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Color getColor() {
        return this.color.getGDXColor();
    }

    @SuppressWarnings("null") // Trust me, it's not null :)
    @Override
    @NotNull
    public String getDescription() {
        return this.description;
    }

    @Override
    @NotNull
    public NamespacedKey getRegistryKey() {
        NamespacedKey key = this.registryKey;
        if (key == null) {
            throw new IllegalStateException("Registry key not yet defined");
        }
        return key;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (this.registryKey != null) {
            throw new IllegalStateException("The registry key may not have been already defined!");
        }
        this.registryKey = key;
    }
}
