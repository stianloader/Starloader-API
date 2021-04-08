/**
 * Actor "Wrapper" APIs. These are used so API methods that consume or return Actors
 * can still be used without having to rely on the galimulator jar.
 * Note that unlike other API interfaces like {@link de.geolykt.starloader.api.empire.ActiveEmpire} or
 * {@link de.geolykt.starloader.api.empire.Star}.
 * they are not fully guaranteed to be extended by either galimulator or starloader and extension
 * may choose to extend themselves. (at their own cost).
 */
package de.geolykt.starloader.api.actor;
