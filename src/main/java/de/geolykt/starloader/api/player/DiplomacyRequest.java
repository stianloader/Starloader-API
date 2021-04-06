package de.geolykt.starloader.api.player;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.empire.ActiveEmpire;

public interface DiplomacyRequest {

    /**
     * Performs {@link #performAction(ActiveEmpire)} but checks if the request ist
     * valid given the context first.
     *
     * @param target The target empire that the action is targeted towards
     * @return The response of the request
     */
    public default String doValidatedly(ActiveEmpire target) {
        ExpectedObfuscatedValueException.requireEmpire(target);
        return isValid(target) ? performAction(target) : "What a strange question.";
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
     */
    public boolean isValid(ActiveEmpire target);

    /**
     * Obtains the response that should be displayed if the action is taken. Note:
     * it is not really recommended to call this method as it avoids the Event
     * listeners. Instead, you might want to use
     * {@link #doValidatedly(ActiveEmpire)}.
     *
     * @param target The target empire that the action is targeted towards
     * @return The response of the request.
     */
    public String performAction(ActiveEmpire target);
}
