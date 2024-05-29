package de.geolykt.starloader.api.player;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;

public interface DiplomacyRequest {

    /**
     * Performs {@link #performAction(de.geolykt.starloader.api.empire.ActiveEmpire)} but checks if the request is
     * valid given the context first.
     *
     * @param target The target empire that the action is targeted towards
     * @return The response of the request
     * @deprecated Use {@link #doValidatedly(Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public default String doValidatedly(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target) {
        return this.isValid(target) ? this.performAction(target) : "What a strange question.";
    }

    /**
     * Performs {@link #performAction(Empire)} but checks if the request is
     * valid given the context first.
     *
     * @param target The target empire that the action is targeted towards
     * @return The response of the request
     * @since 2.0.0
     */
    public default String doValidatedly(@NotNull Empire target) {
        return this.isValid(target) ? this.performAction(target) : "What a strange question.";
    }

    /**
     * Obtains the String that is displayed in the option menu.
     *
     * @return A {@link String} describing the action of the request
     */
    public String getText();

    /**
     * Validates whether it makes any sense to have this request do anything against
     * a certain empire.
     *
     * @param target The target empire that the action is targeted towards
     * @deprecated Use {@link #isValid(Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public boolean isValid(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target);

    /**
     * Validates whether it makes any sense to have this request do anything against
     * a certain empire.
     *
     * @param target The target empire that the action is targeted towards
     * @since 2.0.0
     */
    public boolean isValid(@NotNull Empire target);

    /**
     * Obtains the response that should be displayed if the action is taken. Note:
     * it is not really recommended to call this method as it avoids the Event
     * listeners. Instead, you might want to use
     * {@link #doValidatedly(de.geolykt.starloader.api.empire.ActiveEmpire)}.
     *
     * @param target The target empire that the action is targeted towards
     * @return The response of the request.
     * @deprecated Use {@link #performAction(Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public String performAction(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target);

    /**
     * Obtains the response that should be displayed if the action is taken. Note:
     * it is not really recommended to call this method as it avoids the Event
     * listeners. Instead, you might want to use
     * {@link #doValidatedly(Empire)}.
     *
     * @param target The target empire that the action is targeted towards
     * @return The response of the request.
     * @since 2.0.0
     */
    public String performAction(@NotNull Empire target);
}
