package de.geolykt.starloader.apimixins;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.Gdx;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.actor.ActorConstructionSite;
import de.geolykt.starloader.api.actor.SpawnPredicatesContainer;
import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.actor.ActorConstructionSiteBeginEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxyGeneratingEvent;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.serial.SupportedSavegameFormat;
import de.geolykt.starloader.impl.actors.SelfDestroyingActor;

import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.Settings;
import snoddasmannen.galimulator.Settings.EnumSettings;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.Actor;
import snoddasmannen.galimulator.actors.ShipFactory;
import snoddasmannen.galimulator.actors.StateActorCreator;

/**
 * Mixin to intercept any calls to the static methods within the Galimulator
 * (Space) class. Since sponge's mixins do not support injecting into static
 * methods, it will also perform optimisations to them, if needed.
 */
@Mixin(Space.class)
public class InstanceMixins {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(Space.class);

    @Inject(method = "generateGalaxy", at = @At("HEAD"))
    private static void generateGalaxy(int size, MapData mapData, CallbackInfo ci) {
        EventManager.handleEvent(new GalaxyGeneratingEvent());
    }

    @Overwrite
    public static boolean loadState(String location) {
        Path path = Path.of(location);
        if (Files.notExists(path)) {
            LOGGER.info("Savegame file does not exist: " + location);
            return false;
        }
        LOGGER.info("Loading savegame: " + location);

        if (location.equals("state.dat")) {
            Settings.b("StartedLoading", true);
        }
        Space.setBackgroundTaskDescription("Loading galaxy");

        boolean successful = true;
        try (InputStream is = Files.newInputStream(path)) {
            if (is == null) {
                throw new NullPointerException();
            }
            Galimulator.getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).loadGameState(is);
        } catch (Throwable t) {
            LOGGER.warn("Unable to load savegame", t);
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            successful = false;
        } finally {
            Settings.b("StartedLoading", false);
            Space.setBackgroundTaskDescription(null);
        }

        Drawing.sendBulletin("Welcome back to the galaxy");
        if (EnumSettings.PAUSE_AFTER_LOADING.b() == Boolean.TRUE) {
            Gdx.app.postRunnable(Galimulator::pauseGame);
        }
        return successful;
    }

    @Overwrite
    public static Actor spawnActor(snoddasmannen.galimulator.Star galimStar) {

        if (galimStar.getOwningEmpire().getExtension() != null) {
            Actor var1 = galimStar.getOwningEmpire().getExtension().extendedBuildActor(galimStar);
            if (var1 != null) {
                return var1;
            }
        }

        if (Space.actorSpawnPredicates == null) {
            Space.initializeActorSpawnPredicates();
        }

        SpawnPredicatesContainer container = Galimulator.getStateActorSpawningPredicates();

        if (galimStar.getStarNative() != null) {
            StateActorSpawnPredicate<?> predicate = container.getNatives().get(galimStar.getStarNative());
            if (predicate != null && predicate.test((Star) galimStar)) {
                ShipFactory factory = new ShipFactory((StateActorCreator) predicate.getFactory(), galimStar);
                @SuppressWarnings({ "unchecked", "rawtypes" })
                ActorConstructionSiteBeginEvent<?> evt = new ActorConstructionSiteBeginEvent((ActorConstructionSite) factory, predicate);
                EventManager.handleEvent(evt);
                if (!evt.isCancelled()) {
                    return factory;
                }
            }
        }

        for (StateActorSpawnPredicate<?> predicate : container.getGeneral()) {
            if (predicate.test((Star) galimStar)) {
                ShipFactory factory = new ShipFactory((StateActorCreator) predicate.getFactory(), galimStar);
                @SuppressWarnings({ "unchecked", "rawtypes" })
                ActorConstructionSiteBeginEvent<?> evt = new ActorConstructionSiteBeginEvent((ActorConstructionSite) factory, predicate);
                EventManager.handleEvent(evt);
                if (!evt.isCancelled()) {
                    return factory;
                }
            }
        }

        for (StateActorSpawnPredicate<?> predicate : container.getFallbackShuffled()) {
            if (predicate.test((Star) galimStar)) {
                ShipFactory factory = new ShipFactory((StateActorCreator) predicate.getFactory(), galimStar);
                @SuppressWarnings({ "unchecked", "rawtypes" })
                ActorConstructionSiteBeginEvent<?> evt = new ActorConstructionSiteBeginEvent((ActorConstructionSite) factory, predicate);
                EventManager.handleEvent(evt);
                if (!evt.isCancelled()) {
                    return factory;
                }
            }
        }

        return new SelfDestroyingActor();
    }
}
