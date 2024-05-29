package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.player.DiplomacyRequest;

/**
 * Called when
 * {@link DiplomacyRequest#doValidatedly(de.geolykt.starloader.api.empire.ActiveEmpire)}
 * is called. It is not called when it is force-called but is called before the
 * action has been performed in a non-forcefull manner.
 */
public class DiplomacyRequestEvent extends EmpireEvent {

    @NotNull
    private final DiplomacyRequest request;
    private String response = null;

    /**
     * Constructor.
     *
     * @param target The empire affected by this event.
     * @param request The request that needs to be handled.
     * @deprecated Use {@link #DiplomacyRequestEvent(Empire, DiplomacyRequest)} instead
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public DiplomacyRequestEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target, @NotNull DiplomacyRequest request) {
        super(target);
        this.request = request;
    }

    /**
     * Constructor.
     *
     * @param target The empire affected by this event.
     * @param request The request that needs to be handled.
     * @since 2.0.0
     */
    public DiplomacyRequestEvent(@NotNull Empire target, @NotNull DiplomacyRequest request) {
        super(target);
        this.request = request;
    }

    /**
     * Obtains the {@link DiplomacyRequest} object from which the event was
     * triggered.
     *
     * @return The instance of {@link DiplomacyRequest} that triggered the event.
     */
    @NotNull
    public final DiplomacyRequest getRequest() {
        return this.request;
    }

    /**
     * Obtains the custom response that should appear. If it is null then the
     * default action should be performed.
     *
     * @return The response from the action.
     */
    @Nullable
    public String getResponse() {
        return this.response;
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
