package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;

import snoddasmannen.galimulator.ppclass_167;

public class NamedCheckBoxComponent extends ppclass_167 {

    protected final @NotNull BooleanOption option;

    public NamedCheckBoxComponent(@NotNull BooleanOption option) {
        super(null, option.getName(), option.get(), option.getParent().getName(), null);
        this.option = option;
    }

    public void a(final boolean b) {
        option.set(b);
    }
}
