package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.MapMode.MapModes;

@Mixin(MapModes.class)
public class MapModesMixins implements MapMode {

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the Map mode registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initMapModes();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static MapModes valueOf(String var0) {
        if (var0 == null) {
            return null;
        }
        return (MapModes) Registry.MAP_MODES.getIntern(var0);
    }

    @Overwrite
    public static MapModes[] values() {
        return (MapModes[]) Registry.MAP_MODES.getValues();
    }

    @Unique
    @Nullable
    private NamespacedKey registryKey;

    @Shadow
    private boolean showsActors;

    @Override
    @NotNull
    public TextureRegion getIcon() {
        TextureRegion tr = ((MapModes) (Object) this).icon;
        if (tr == null) {
            throw new IllegalStateException("Internal error");
        }
        return tr;
    }

    @Override
    @NotNull
    public NamespacedKey getRegistryKey() {
        NamespacedKey key = registryKey;
        if (key == null) {
            throw new IllegalStateException("Registry key not yet set.");
        }
        return key;
    }

    @Override
    public boolean renderActors() {
        return showsActors;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (registryKey != null) {
            throw new IllegalStateException("Registry key already set!");
        }
        registryKey = key;
    }
}
