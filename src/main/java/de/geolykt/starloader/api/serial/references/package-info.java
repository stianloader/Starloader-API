/**
 * Collection of persistent reference objects that reference transient objects that cannot (or rather shouldn't) be
 * serialised separately. In essence they are lazy objects that may copy frequently used data
 * as a fallback when the lazy object cannot lookup the object it desires to hold.
 *
 * @since 2.0.0-a20251224
 */
@org.jetbrains.annotations.ApiStatus.AvailableSince("2.0.0-a20251224")
package de.geolykt.starloader.api.serial.references;
