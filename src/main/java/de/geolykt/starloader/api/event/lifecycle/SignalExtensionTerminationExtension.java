package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.StarloaderAPIExtension;
import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.mod.Extension;

/**
 * Event that <b>may</b> be fired by <b>some</b> Extensions when they are in the process of unloading.
 * This has a significant usecase when Extensions are unloaded, that you depended on.
 *<br/>
 * Due to how SLL Classloading works, the entirety of the SLAPI becomes unavailable after this event is
 * fired with a {@link StarloaderAPIExtension} as the targeted extension.
 */
public class SignalExtensionTerminationExtension extends Event {
    protected final Extension unloadingExtension;

    public SignalExtensionTerminationExtension(Extension unloadingExtension) {
        this.unloadingExtension = unloadingExtension;
    }

    public Extension getUnloadingExtension() {
        return unloadingExtension;
    }
}
