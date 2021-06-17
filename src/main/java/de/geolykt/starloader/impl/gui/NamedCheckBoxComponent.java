package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

import snoddasmannen.galimulator.hj;

public class NamedCheckBoxComponent extends hj implements ScreenComponent {

    protected final @NotNull Screen parent;
    protected final @NotNull BooleanOption option;

    public NamedCheckBoxComponent(@NotNull Screen parent, @NotNull BooleanOption option) {
        super(null, option.getName(), option.get(), option.getParent().getName(), null);
        this.parent = NullUtils.requireNotNull(parent);
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
