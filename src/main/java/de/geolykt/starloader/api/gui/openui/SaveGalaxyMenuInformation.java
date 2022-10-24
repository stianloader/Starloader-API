package de.geolykt.starloader.api.gui.openui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.serial.SavegameFormat;
import de.geolykt.starloader.api.serial.SupportedSavegameFormat;

class SaveGalaxyMenuInformation {
    @NotNull
    SavegameFormat saveFormat = Galimulator.getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE);

    @Nullable
    String searchSelector = null;
}
