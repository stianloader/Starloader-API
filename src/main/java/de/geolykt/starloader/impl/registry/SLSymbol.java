package de.geolykt.starloader.impl.registry;

import snoddasmannen.galimulator.FlagItem;

public class SLSymbol extends FlagItem.BuiltinSymbols {

    private static final long serialVersionUID = -3047139715367932860L;

    public SLSymbol(String enumName, int ordinal, String texture, boolean mustBeFixed, int fixedWidth, int fixedHeight) {
        super(enumName, ordinal, texture, mustBeFixed, fixedWidth, fixedHeight);
    }
}
