package de.geolykt.starloader.impl;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.GameConfiguration;

import snoddasmannen.galimulator.DeviceConfiguration;
import snoddasmannen.galimulator.Galemulator;
import snoddasmannen.galimulator.Settings.EnumSettings;

public class GalimulatorConfiguration implements GameConfiguration {

    @Override
    public boolean allowAWBA() {
        return (boolean) EnumSettings.ALLOW_ALL_WILL_BE_ASHES.getValue();
    }

    @Override
    public boolean allowBloodPurge() {
        return (boolean) EnumSettings.ALLOW_BLOOD_PURGE.getValue();
    }

    @Override
    public boolean allowDegeneration() {
        return (boolean) EnumSettings.ALLOW_DEGENERATION.getValue();
    }

    @Override
    public boolean allowTranscendence() {
        return (boolean) EnumSettings.ALLOW_TRANSCENDENCE.getValue();
    }

    @Override
    @ApiStatus.AvailableSince("2.0.0-a20251218.2")
    @Contract(pure = true)
    public boolean getDrawNeutralStars() {
        return (Boolean) EnumSettings.DRAW_NEUTRAL_STARS.getValue();
    }

    @Override
    public int getMinimumComponentHeight() {
        return DeviceConfiguration.getConfiguration().getMinHeight();
    }

    @Override
    public int getShipMultiplier() {
        return (int) EnumSettings.SHIP_NUMBER_MOD.getValue();
    }

    @Override
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    public double getTargetTPS() {
        return ((Number) EnumSettings.TARGET_TPS_RATIO.getValue()).doubleValue();
    }

    @Override
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    public double getTimelapseModifier() {
        return ((Number) EnumSettings.FIXED_TPF_RATIO.getValue()).doubleValue();
    }

    @Override
    public int getTranscendceLevel() {
        return (int) EnumSettings.TRANSCEND_LEVEL.getValue();
    }

    @Override
    @NotNull
    @ApiStatus.AvailableSince("2.0.0-a20251218.2")
    @Contract(pure = false, value = "_ -> this")
    public GameConfiguration setDrawNeutralStars(boolean value) {
        EnumSettings.DRAW_NEUTRAL_STARS.a(value);
        return this;
    }

    @Override
    @NotNull
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    @Contract(pure = false, value = "_ -> this")
    public GameConfiguration setTargetTPS(int tps) {
        EnumSettings.TARGET_TPS_RATIO.a(Integer.valueOf(tps));
        return this;
    }

    @Override
    @NotNull
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    @Contract(pure = false, value = "_ -> this")
    public GameConfiguration setTimelapseModifier(int modifier) {
        Object tps = EnumSettings.TARGET_TPS_RATIO.getValue();
        // setTimelapseModifier(int) resets the TPS ratio, which is why we record the previous TPS.
        Galemulator.setTimelapseModifier(modifier);
        EnumSettings.TARGET_TPS_RATIO.a(tps);
        return this;
    }
}
