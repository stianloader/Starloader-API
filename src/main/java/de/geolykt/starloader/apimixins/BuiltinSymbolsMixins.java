package de.geolykt.starloader.apimixins;

import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;

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
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.FlagSymbol;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.FlagItem.BuiltinSymbols;

@Mixin(value = BuiltinSymbols.class, priority = 0)
public class BuiltinSymbolsMixins implements FlagSymbol {

    @Overwrite
    public static BuiltinSymbols b() {
        BuiltinSymbols[] var0 = (BuiltinSymbols[]) Registry.FLAG_SYMBOLS.getValues();
        return var0[ThreadLocalRandom.current().nextInt(var0.length)];
    }

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the empire specials registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initFlagSymbols();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static BuiltinSymbols valueOf(String var0) {
        if (var0 == null) {
            return null;
        }
        return (BuiltinSymbols) Registry.FLAG_SYMBOLS.getIntern(var0);
    }

    @Overwrite
    public static BuiltinSymbols[] values() {
        return (BuiltinSymbols[]) Registry.FLAG_SYMBOLS.getValues();
    }

    @Shadow
    private int fixedH;

    @Shadow
    private int fixedW;

    @Shadow
    private boolean mustBeSquare;

    @Unique
    @Nullable
    private NamespacedKey registryKey = null;

    @Shadow
    private TextureRegion texture;

    @Override
    public @NotNull OptionalInt getHeight() {
        return fixedH != 0 ? NullUtils.getOptionalInt(fixedH) : NullUtils.getEmptyOptionalInt();
    }

    @Override
    public @NotNull NamespacedKey getRegistryKey() {
        NamespacedKey key = registryKey;
        if (key == null) {
            throw new IllegalStateException("Registry key not yet defined");
        }
        return key;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull TextureRegion getTexture() {
        return texture;
    }

    @Override
    public @NotNull OptionalInt getWidth() {
        return fixedW != 0 ? NullUtils.getOptionalInt(fixedW) : NullUtils.getEmptyOptionalInt();
    }

    @Override
    public boolean isSquare() {
        return mustBeSquare;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (registryKey != null) {
            throw new IllegalStateException("The registry key is already set!");
        }
        registryKey = key;
    }
}
