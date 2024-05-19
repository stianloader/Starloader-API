/**
 * A dimension in the sense of SLAPI is a container which can contain the board state:
 * That is, what stars are present, which empires and factions exist, etc.
 *
 * <p>In vanilla galimulator, there can only be one dimension at the same time.
 * Truthfully, SLAPI is also designed in a way where there can only really be a single
 * dimension at the same time; however, this package and it's contents exist
 * as a way of reducing the amount of work that is needed to refractor to
 * a system that would allow multiple dimensions to exist.
 *
 * <p>Further, this package exist in order to reduce excess bloat from
 * {@link de.geolykt.starloader.api.Galimulator} or similar classes.
 */
package de.geolykt.starloader.api.dimension;
