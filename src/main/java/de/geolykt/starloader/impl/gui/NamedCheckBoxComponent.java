package de.geolykt.starloader.impl.gui;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

import snoddasmannen.galimulator.hj;

public class NamedCheckBoxComponent extends hj implements ScreenComponent {

    protected final Screen parent;
    protected final BooleanOption option;

    public NamedCheckBoxComponent(@NotNull Screen parent, @NotNull BooleanOption option) {
        super(null, option.getName(), option.get(), option.getParent().getName(), null);
        this.parent = Objects.requireNonNull(parent);
        this.option = option;
    }

    public void a(final boolean b) {
        option.set(b);
    }

    @Override
    public @NotNull Screen getParentScreen() {
        return parent;
    }
}
