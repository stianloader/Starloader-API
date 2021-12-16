package de.geolykt.starloader.impl;

import static snoddasmannen.galimulator.Settings.EnumSettings.ALLOW_ALL_WILL_BE_ASHES;
import static snoddasmannen.galimulator.Settings.EnumSettings.ALLOW_BLOOD_PURGE;
import static snoddasmannen.galimulator.Settings.EnumSettings.ALLOW_DEGENERATION;
import static snoddasmannen.galimulator.Settings.EnumSettings.ALLOW_TRANSCENDENCE;
import static snoddasmannen.galimulator.Settings.EnumSettings.SHIP_NUMBER_MOD;
import static snoddasmannen.galimulator.Settings.EnumSettings.TRANSCEND_LEVEL;

import de.geolykt.starloader.api.GameConfiguration;

public class GalimulatorConfiguration implements GameConfiguration {

    @Override
    public boolean allowAWBA() {
        return (boolean) ALLOW_ALL_WILL_BE_ASHES.b();
    }

    @Override
    public boolean allowBloodPurge() {
        return (boolean) ALLOW_BLOOD_PURGE.b();
    }

    @Override
    public boolean allowDegeneration() {
        return (boolean) ALLOW_DEGENERATION.b();
    }

    @Override
    public boolean allowTranscendence() {
        return (boolean) ALLOW_TRANSCENDENCE.b();
    }

    @Override
    public int getMinimumComponentHeight() {
        return snoddasmannen.galimulator.class_27.getConfiguration().getMinHeight();
    }

    @Override
    public int getShipMultiplier() {
        return (int) SHIP_NUMBER_MOD.b();
    }

    @Override
    public int getTranscendceLevel() {
        return (int) TRANSCEND_LEVEL.b();
    }
}
