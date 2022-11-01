package de.geolykt.starloader.extras.impl.tick;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.MathUtils;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.extras.api.event.StarTickEvent;

import snoddasmannen.galimulator.Settings.EnumSettings;
import snoddasmannen.galimulator.Debris;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Settings;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.Sprawl;
import snoddasmannen.galimulator.Sprawl.SprawlType;
import snoddasmannen.galimulator.effects.AuraEffect;

public class StarTickingImplementation {

    private static int random(int bound) {
        return (int) (Math.random() * bound);
    }

    public static void tick(final snoddasmannen.galimulator.@NotNull Star galimStar) {
        final Star slStar = (Star) galimStar;
        // TODO warning: following line may cause an NPE
        for (TickCallback<Star> callback : slStar.getTickCallbacks()) {
            callback.tick(slStar);
        }

        // Following code is based on decompiled galimulator code - handle with care!
        galimStar.getOwningEmpire().addTransientWealth(galimStar.getWealth());

        StarTickEvent evt = new StarTickEvent(slStar, MathUtils.random() <= 0.05F);
        EventManager.handleEvent(evt);
        if (!evt.isActive()) {
            return; // Inactive tick
        }

        if (MathUtils.randomBoolean(0.1F)) {
            galimStar.refreshBeaconState();
        }

        slStar.addLocalDevelopment(20 * galimStar.getOwningEmpire().getDevelopmentGrowthRate());
        slStar.setHeat(slStar.getHeat() * 0.99F);

        float wealthDecay = 0.003F * galimStar.getOwningEmpire().getWealthDecayFactor();
        float wealth = slStar.getWealth();
        wealth *= (1F - wealthDecay);

        List<Star> neighbours = slStar.getNeighbourList();
        if (!neighbours.isEmpty()) {
            float neighbourWealth = 0.0F;
            for (Star s : neighbours) {
                neighbourWealth += s.getWealth();
            }
            wealth += (neighbourWealth / neighbours.size() - wealth) * 0.002F;
        }

        if (wealth == Float.NaN) {
            wealth = 1.0F;
        }
        slStar.setWealth(wealth);

        if (EnumSettings.SPRAWL.getValue() == Boolean.TRUE) {
            ActiveEmpire assignedEmpire = slStar.getAssignedEmpire();
            float sprawlLevel = slStar.getSprawlLevel();

            sprawlLevel *= 0.997F; // Apply base decay
            if (assignedEmpire.getCapitalID() == slStar.getUID() && assignedEmpire != Galimulator.getNeutralEmpire()) {
                sprawlLevel += 0.5F + assignedEmpire.getStarCount() / 500.0F;
            }

            if (galimStar.getArtifact() != null) {
                sprawlLevel += 0.2F;
            }

            if (wealth > 3F) {
                sprawlLevel += (wealth - 3F) / 200.0F;
            }

            if (sprawlLevel > 100F) {
                sprawlLevel = 100F;
            }

            float neighbourSprawl = 0F;
            for (Star neighbour : neighbours) {
                neighbourSprawl += neighbour.getSprawlLevel();
            }
            sprawlLevel += (neighbourSprawl / neighbours.size() - sprawlLevel) * 0.022F;

            ArrayList<Sprawl> sprawls = galimStar.sprawls;
            if (sprawls == null) {
                sprawls = new ArrayList<>();
            }

            int sprawlCount = sprawls.size();
            if ((sprawlCount + 1) * 5F < sprawlLevel) {
                // Underpopulation
                sprawls.add(new Sprawl(SprawlType.HABITAT, galimStar));
            } else if (sprawlCount * 5F > sprawlLevel) {
                // Overpopulation
                // The sprawl level can never be negative, so we don't have to fear an NPE there
                sprawls.remove(sprawlCount - 1).setAlive(false);
            }

            galimStar.sprawls = sprawls;
            slStar.setSprawlLevel(sprawlLevel);
            sprawls.forEach(Sprawl::activity);
        }

        if (galimStar.orbitingActors == null) {
            galimStar.orbitingActors = new HashSet<>();
        } else {
            galimStar.orbitingActors.clear();
        }

        if (slStar.getAssignedEmpire() == Galimulator.getNeutralEmpire()
                && galimStar.getStarNative() != null
                && MathUtils.randomBoolean(0.001F)) { // 0.1% chance, so on average once every 1000 active ticks or once every 20 millenias
            Space.addActor(galimStar.getStarNative().spawnNativeActor(galimStar));
        }

        if (slStar.getInternalRandom().nextInt((int) Settings.EnumSettings.EXPANSION_MODIFIER.getValue()) == 0) {
            Vector<snoddasmannen.galimulator.Star> foreignConnections = galimStar.getForeignConnections(false);
            if (!foreignConnections.isEmpty()) {
                snoddasmannen.galimulator.Star target = foreignConnections.get(random(foreignConnections.size()));
                if (galimStar.getOwningEmpire().canAttack(target, galimStar)) {
                    slStar.setHeat(slStar.getHeat() + 1);
                    Star targetSlStar = (Star) target;
                    targetSlStar.setHeat(targetSlStar.getHeat() + 1);
                    float targetX = targetSlStar.getX();
                    float targetY = targetSlStar.getY();
                    float rotation = (float) (Math.atan2(slStar.getY() - targetY, slStar.getX() - targetX) + Math.PI);
                    int spawnedParticles = slStar.getInternalRandom().nextInt(4); // Yes, galimulator deals with it differently, but this is the intended code
                    GalColor ownerColor = galimStar.getOwningEmpire().getColor();
                    for (int i = 0; i < spawnedParticles; i++) {
                        Space.showItem(new Debris(targetX, targetY, ownerColor, rotation, 0.3F));
                    }
                    float sizeFactor = snoddasmannen.galimulator.Star.globalSizeFactor;
                    Space.showItem(new AuraEffect(targetX, targetY, sizeFactor * 1.1D, false, sizeFactor / 200D, 100, ownerColor));

                    int attackerPower = galimStar.getOwningEmpire().getCombatPower(galimStar, target, true);
                    int defenderPower = target.getOwningEmpire().getCombatPower(target, galimStar, false);
                    if (attackerPower > defenderPower || MathUtils.randomBoolean(0.01F)) {
                        snoddasmannen.galimulator.Empire oldTargetEmpire = target.getOwningEmpire();
                        targetSlStar.doTakeover(slStar.getAssignedEmpire());
                        if (((ActiveEmpire) oldTargetEmpire).getStarCount() == 0
                                && /* TODO achivements! */ true) {
                            // TODO
                        }
                    }
                }
            }
        }
        galimStar.tick(); // FIXME remove in prod (may cause deadlock)
    }
}
