package de.geolykt.starloader.api.actor;

/**
 * Interface that is implemented by the flagship, a player controlled actor. The actor will shoot on it's own, but will not
 * generate orders on it's own, that is it does not have any meaningful pathfinding abilities.
 * All player-controlled empires have this ship at all times and generally it will be destroyed as soon as the owning empire
 * is no longer the player's empire. As such, there can be only one flagship at any time.
 *
 * @author Geolykt
 * @since 2.0.0
 */
public interface Flagship extends StateActor {
}
