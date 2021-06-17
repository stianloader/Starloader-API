package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.empire.DiplomacyRequestEvent;
import de.geolykt.starloader.api.player.DiplomacyRequest;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.diplomacy.PlayerRequest;

@Mixin(PlayerRequest.class)
public class DiploRequestMixins implements DiplomacyRequest {

    @Shadow
    public String a() {
        return "";
    }

    @Shadow
    public boolean a(final Empire empire) {
        return empire.ae();
    }

    @Shadow
    public String b(final Empire empire) {
        return empire.toString();
    }

    @Overwrite
    public String c(final Empire empire) {
        if (empire == null) {
            return "What a strange question.";
        }
        DiplomacyRequestEvent event = new DiplomacyRequestEvent((ActiveEmpire) empire, this);
        EventManager.handleEvent(event);
        if (event.getResponse() != null) {
            return event.getResponse();
        }
        return this.a(empire) ? this.b(empire) : "What a strange question.";
    }

    @Override
    public String doValidatedly(ActiveEmpire target) {
        return c(ExpectedObfuscatedValueException.requireEmpire(target));
    }

    @Override
    public String getText() {
        return a();
    }

    @Override
    public boolean isValid(ActiveEmpire target) {
        return a(ExpectedObfuscatedValueException.requireEmpire(target));
    }

    @Override
    public String performAction(ActiveEmpire target) {
        return b(ExpectedObfuscatedValueException.requireEmpire(target));
    }
}
