package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.empire.DiplomacyRequestEvent;
import de.geolykt.starloader.api.player.DiplomacyRequest;
import de.geolykt.starloader.api.registry.RegistryKeys;

import snoddasmannen.galimulator.diplomacy.PlayerRequest;

@Mixin(PlayerRequest.class)
public class DiploRequestMixins implements DiplomacyRequest {

    @Shadow
    public String getName() {
        return "";
    }

    @Shadow
    public boolean a(final snoddasmannen.galimulator.Empire empire) { // isNotTranscending
        return !((Empire) empire).getState().equals(RegistryKeys.GALIMULATOR_TRANSCENDING);
    }

    @Shadow
    public String b(final snoddasmannen.galimulator.Empire empire) {
        return empire.toString();
    }

    @Overwrite
    public String c(final snoddasmannen.galimulator.Empire empire) {
        if (empire == null) {
            return "What a strange question.";
        }
        DiplomacyRequestEvent event = new DiplomacyRequestEvent((Empire) empire, this);
        EventManager.handleEvent(event);
        if (event.getResponse() != null) {
            return event.getResponse();
        }
        return this.a(empire) ? this.b(empire) : "What a strange question.";
    }

    @Override
    @Deprecated
    public String doValidatedly(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target) {
        return this.c(ExpectedObfuscatedValueException.requireEmpire((Empire) target));
    }

    @Override
    public String getText() {
        return this.getName();
    }

    @Override
    @Deprecated
    public boolean isValid(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target) {
        return this.a(ExpectedObfuscatedValueException.requireEmpire((Empire) target));
    }

    @Override
    @Deprecated
    public String performAction(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target) {
        return this.b(ExpectedObfuscatedValueException.requireEmpire((Empire) target));
    }

    @Override
    public boolean isValid(@NotNull Empire target) {
        return this.a((snoddasmannen.galimulator.Empire) target);
    }

    @Override
    public String performAction(@NotNull Empire target) {
        return this.b((snoddasmannen.galimulator.Empire) target);
    }
}
