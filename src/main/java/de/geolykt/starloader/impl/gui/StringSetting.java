package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.UserSettings$SettingType;
import snoddasmannen.galimulator.ht;

public class StringSetting extends ht {

    protected final StringOption option;

    public StringSetting(StringOption opt) {
        super(opt.getName(), UserSettings$SettingType.b, null);
        option = opt;
    }

    @Override
    public void a(Object o) {
        option.set(o.toString());
    }

    @Override
    public Object b() {
        return option.get();
    }

    @Override
    public Vector d_() {
        return new Vector<>(option.getRecommendedValues());
    }

    @Override
    public String f() {
        return option.getParent().getName();
    }
}
