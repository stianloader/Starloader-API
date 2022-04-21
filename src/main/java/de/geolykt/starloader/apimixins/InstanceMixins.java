package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.GalaxyGeneratingEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxyLoadingEvent;

import snoddasmannen.galimulator.Space;

/**
 * Mixin to intercept any calls to the static methods within the Galimulator
 * (Space) class. Since sponge's mixins do not support injecting into static
 * methods, it will also perform optimisations to them, if needed.
 */
@Mixin(Space.class)
public class InstanceMixins {

    @Overwrite
    public static void setBackgroundTaskDescription(final String j) {
        // this method is called to show the progress of things happening to the user
        // however we can exploit this behaviour to create events without having to overwrite large static methods
        // one day we will have a custom ASM injector for that, but that is something for later.
        // Though I am confused on why the hell this method exists upstream, as it only performs few, rather easy to do operations
        Event evt = null;
        switch (j) {
        case "Generating galaxy":
            evt = new GalaxyGeneratingEvent();
            break;
        case "Loading galaxy: Reading file":
        case "Loading galaxy": // The XStream and non-X-Stream messages differ here
            evt = new GalaxyLoadingEvent();
            break;
        default:
            break;
        }
        if (evt != null) {
            EventManager.handleEvent(evt);
        }
        Space.backgroundTaskDescription = j;
        Space.K = null;
    }
}
