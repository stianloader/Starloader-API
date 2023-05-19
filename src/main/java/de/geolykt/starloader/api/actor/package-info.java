/**
 * Actor "Wrapper" APIs. These are used so API methods that consume or return Actors
 * can still be used without having to rely on the galimulator jar.
 * Note that like other API interfaces like {@link de.geolykt.starloader.api.empire.ActiveEmpire} or
 * {@link de.geolykt.starloader.api.empire.Star} extensions should not implement any API interfaces
 * themselves unless they know what they are doing (at which point the galimulator Actor class
 * or a subclass thereof should be extended too).
 */
package de.geolykt.starloader.api.actor;
