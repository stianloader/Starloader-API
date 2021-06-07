package de.geolykt.starloader.impl;

import static snoddasmannen.galimulator.Settings$EnumSettings.*;

import de.geolykt.starloader.api.GameConfiguration;

public class GalimulatorConfiguration implements GameConfiguration {

    @Override
    public boolean allowAWBA() {
        return (boolean) b.b();
    }

    @Override
    public boolean allowBloodPurge() {
        return (boolean) a.b();
    }

    @Override
    public boolean allowDegeneration() {
        return (boolean) e.b();
    }

    @Override
    public boolean allowTranscendence() {
        return (boolean) c.b();
    }

    @Override
    public int getShipMultiplier() {
        return (int) H.b();
    }

    @Override
    public int getTranscendceLevel() {
        return (int) q.b();
    }
}
