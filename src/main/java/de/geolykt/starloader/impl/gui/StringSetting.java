package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.Screen;
import de.geolykt.starloader.api.gui.modconf.StrictStringOption;
import de.geolykt.starloader.api.gui.modconf.StringChooseOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.UserSettings$SettingType;
import snoddasmannen.galimulator.ht;

public class StringSetting extends ht {

    protected final StringOption option;
    protected final Screen screen;

    public StringSetting(Screen screen, StringOption opt) {
        super(opt.getName(), UserSettings$SettingType.b, null);
        option = opt;
        this.screen = screen;
    }

    @Override
    public void a(Object o) {
        if ("Custom".equals(o.toString()) && !(option instanceof StringChooseOption)) {
            var builder = Drawing.textInputBuilder("Change value of setting", option.get(), option.getName());
            if (option instanceof StrictStringOption) {
                builder.addHook(text -> {
                    if (((StrictStringOption) option).isValid(text)) {
                        option.set(text);
                        screen.markDirty();
                    } else {
                        Drawing.toast("The input value is not valid!");
                    }
                });
            } else {
                builder.addHook(text -> {
                    option.set(text);
                    screen.markDirty();
                });
            }
            builder.build();
            return;
        }
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
