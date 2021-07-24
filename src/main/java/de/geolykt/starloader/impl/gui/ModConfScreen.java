package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationSection;
import de.geolykt.starloader.api.gui.modconf.ModConf.ModConfSpec;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.api.gui.modconf.NumberOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ck;
import snoddasmannen.galimulator.b.class_s;

public class ModConfScreen implements ck, Screen {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ModConfScreen.class);

    protected final ModConfSpec config;
    protected boolean dirty = false;

    public ModConfScreen(ModConfSpec config) {
        this.config = config;
    }

    @Override
    public void addChild(@NotNull ScreenComponent o) {
        throw new UnsupportedOperationException("Cannot add new children to this class.");
    }

    @Override
    public boolean canAddChildren() {
        return false;
    }

    @SuppressWarnings({ "all" })
    @Override
    public @NotNull List<@NotNull ScreenComponent> getChildren() {
        List<@NotNull ScreenComponent> ret = (List) getItems();
        if (ret == null) {
            throw new NullPointerException();
        }
        return ret;
    }

    @Override
    public int getInspectorWidth() {
        return 600;
    }

    @Override
    public ArrayList<class_s> getItems() {
        ArrayList<class_s> alist = new ArrayList<>();
        for (ConfigurationSection section : config.getSections()) {
            for (ConfigurationOption<?> option : section.getChildren()) {
                if (option instanceof BooleanOption) {
                    alist.add(new NamedCheckBoxComponent(this, (BooleanOption) option));
                } else if (option instanceof NumberOption<?>) {
                    alist.add(new NamedIntegerChooserComponent(this, (NumberOption<?>) option));
                } else if (option instanceof StringOption) {
                    alist.add(new NamedStringChooserComponent(this, (StringOption) option));
                } else {
                    LOGGER.info("Unsupported option of class: " + option.getClass().getName());
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
