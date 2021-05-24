package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.Screen;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.gui.modconf.NumberOption;

import snoddasmannen.galimulator.UserSettings$SettingType;
import snoddasmannen.galimulator.ht;

// Floating point numbers could be an issue since Galimulator does not use them
public class NumericSetting extends ht {

    protected final NumberOption<Number> option;
    protected final Screen screen;

    public NumericSetting(Screen screen, NumberOption<Number> opt) {
        super(opt.getName(), UserSettings$SettingType.c, null);
        option = opt;
        this.screen = screen;
    }

    @Override
    public void a(Object o) {
        if ("Custom".equals(o.toString())) {
            var builder = Drawing.textInputBuilder("Change value of setting", option.get().toString(), option.getName());
            builder.addHook(text -> {
                Double d;
                try {
                    d = Double.valueOf(text);
                } catch (NumberFormatException e) {
                    Drawing.toast(String.format("%s is not a valid number.", text));
                    return;
                }
                if (d < option.getMinimum().doubleValue()) {
                    Drawing.toast(String.format("%s is below the minimum value of %s", text, option.getMinimum().toString()));
                    return;
                }
                if (d > option.getMaximum().doubleValue()) {
                    Drawing.toast(String.format("%s is above the maximum value of %s", text, option.getMaximum().toString()));
                    return;
                }
                NumberOption<?> rawOpt = option; // We have to trick the compiler in order to get this to compile.
                if (rawOpt instanceof FloatOption) {
                    ((FloatOption) rawOpt).setValue(d.floatValue());
                    screen.markDirty();
                } else if (rawOpt instanceof IntegerOption) {
                    ((IntegerOption) rawOpt).setValue(d.intValue());
                    screen.markDirty();
                } else {
                    option.set(d);
                    screen.markDirty();
                }
            });
            builder.build();
            return;
        }
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
