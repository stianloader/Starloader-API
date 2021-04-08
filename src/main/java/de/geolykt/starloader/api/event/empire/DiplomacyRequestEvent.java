package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.player.DiplomacyRequest;

/**
 * Called when
 * {@link DiplomacyRequest#doValidatedly(de.geolykt.starloader.api.empire.ActiveEmpire)}
 * is called. It is not called when it is force-called but is called before the
 * action has been performed in a non-forcefull manner.
 */
public class DiplomacyRequestEvent extends EmpireEvent {

    private final DiplomacyRequest request;
    private String response = null;

    public DiplomacyRequestEvent(@NotNull ActiveEmpire target, @NotNull DiplomacyRequest request) {
        super(target);
        this.request = request;
    }

    /**
     * Obtains the {@link DiplomacyRequest} object from which the event was
     * triggered.
     *
     * @return The instance of {@link DiplomacyRequest} that triggered the event.
     */
    public final DiplomacyRequest getRequest() {
        return request;
    }

    /**
     * Obtains the custom response that should appear. If it is null then the
     * default action should be performed.
     *
     * @return The response from the action.
     */
    public @Nullable String getResponse() {
        return response;
    }

    /**
     * Sets the custom response that should appear. A null response results in the
     * default action being performed.
     *
     * @param response The response from the action that should be displayed.
     */
    public void setResponse(@Nullable String response) {
        this.response = response;
    }
}
