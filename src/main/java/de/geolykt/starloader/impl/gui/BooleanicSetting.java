package de.geolykt.starloader.impl.gui;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;

import snoddasmannen.galimulator.UserSettings$SettingType;
import snoddasmannen.galimulator.ht;

public class BooleanicSetting extends ht {

    protected final BooleanOption option;

    public BooleanicSetting(BooleanOption opt) {
        super(opt.getName(), UserSettings$SettingType.a, null);
        option = opt;
    }

    @Override
    public void a(Object o) {
        if (!(o instanceof Boolean)) {
            throw new IllegalArgumentException("Expected a boolean!");
        }
        option.set((Boolean) o);
    }

    @Override
    public Object b() {
        return option.get();
    }

    @Override
    public String f() {
        return option.getParent().getName();
    }
}
