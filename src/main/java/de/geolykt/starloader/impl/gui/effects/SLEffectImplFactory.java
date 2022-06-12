package de.geolykt.starloader.impl.gui.effects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.gui.effects.EffectFactory;
import de.geolykt.starloader.api.gui.effects.LocationSelectEffect;

public class SLEffectImplFactory extends EffectFactory {

    @Override
    @NotNull
    protected LocationSelectEffect createLocationSelectEffect(@NotNull Locateable location) {
        return new SLLocationSelectEffect(location);
    }
}
