package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.DestroyerSpec;

import snoddasmannen.galimulator.actors.Destroyer;

/**
 * Mixins into the destroyer class implementing the destroyer actor specification.
 */
@Mixin(Destroyer.class)
public class DestroyerMixins extends StateActorMixins implements DestroyerSpec {
}
