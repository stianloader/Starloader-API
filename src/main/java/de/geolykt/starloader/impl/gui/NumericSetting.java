package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import de.geolykt.starloader.api.gui.modconf.NumberOption;

import snoddasmannen.galimulator.UserSettings$SettingType;
import snoddasmannen.galimulator.ht;

// Floating point numbers could be an issue since Galimulator does not use them
public class NumericSetting extends ht {

    protected final NumberOption<Number> option;

    public NumericSetting(NumberOption<Number> opt) {
        super(opt.getName(), UserSettings$SettingType.c, null);
        option = opt;
    }

    @Override
    public void a(Object o) {
        if (!(o instanceof Number)) {
            throw new IllegalArgumentException("Expected a numeric value!");
        }
        option.set((Number) o);
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
