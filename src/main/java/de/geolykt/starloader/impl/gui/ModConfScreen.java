package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.gui.Screen;
import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationSection;
import de.geolykt.starloader.api.gui.modconf.IntegerChooseOption;
import de.geolykt.starloader.api.gui.modconf.ModConf.ModConfSpec;
import de.geolykt.starloader.api.gui.modconf.NumberOption;
import de.geolykt.starloader.api.gui.modconf.StringChooseOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ck;
import snoddasmannen.galimulator.hj;
import snoddasmannen.galimulator.hk;
import snoddasmannen.galimulator.hl;

public class ModConfScreen implements ck, Screen {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ModConfScreen.class);

    protected final ModConfSpec config;
    protected boolean dirty = false;

    public ModConfScreen(ModConfSpec config) {
        this.config = config;
    }

    @Override
    public int getInspectorWidth() {
        return 600;
    }

    @Override
    public ArrayList getItems() {
        ArrayList<Object> alist = new ArrayList<>();
        for (ConfigurationSection section : config.getSections()) {
            for (ConfigurationOption<?> option : section.getChildren()) {
                String name = option.getName();
                String cat = option.getParent().getName();
                Object value = option.get();

                if (option instanceof BooleanOption) {
                    BooleanicSetting setting = new BooleanicSetting((BooleanOption) option);

                    alist.add(new hj(null, name, (boolean) value, cat, setting));
                } else if (option instanceof NumberOption<?>) {

                    @SuppressWarnings("unchecked")
                    NumericSetting setting = new NumericSetting(this, (NumberOption<Number>) option);

                    final Vector<Object> options = new Vector<>(((NumberOption<?>) option).getRecommendedValues());
                    if (!(option instanceof IntegerChooseOption)) {
                        options.add("Custom");
                    }
                    alist.add(new hl(null, name, Integer.toString((int) setting.b()), options, cat, setting));
                } else if (option instanceof StringOption) {

                    StringSetting setting = new StringSetting(this, (StringOption) option);

                    final Vector<Object> options = new Vector<>(((StringOption) option).getRecommendedValues());
                    if (!(option instanceof StringChooseOption)) {
                        options.add("Custom");
                    }
                    alist.add(new hk(null, name, (String) value, options, cat, setting));
                }
            }
        }
        dirty = false;
        return alist;
    }

    @Override
    public String getTitle() {
        return "Mod Settings";
    }

    @Override
    public GalColor getTitlebarColor() {
        return GalColor.ORANGE;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public boolean isValid() {
        return !dirty;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }
}
