package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;

import snoddasmannen.galimulator.dialog.LabeledCheckboxComponent;

public class NamedCheckBoxComponent extends LabeledCheckboxComponent {

    protected final @NotNull BooleanOption option;

    public NamedCheckBoxComponent(@NotNull BooleanOption option) {
        super(option.getName(), option.get(), option.getParent().getName());
        this.option = option;
    }

    @Override
    public void a(final boolean b) { // TODO deobfuscate the name of this method. Should be fairly straightforward
        super.a(b);
        option.set(b);
    }
}
