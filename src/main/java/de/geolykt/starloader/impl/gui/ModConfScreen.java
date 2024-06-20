package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.ConfigurationSection;
import de.geolykt.starloader.api.gui.modconf.ModConf.ModConfSpec;
import de.geolykt.starloader.api.gui.modconf.NumberOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.Dialog;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.dialog.DialogComponent;

public class ModConfScreen implements Dialog {

    /**
     * Internal logger instance for this class.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModConfScreen.class);

    /**
     * The ModConf implementation that is used by this screen. This class
     * does not care what implements this interface.
     */
    protected final ModConfSpec config;

    /**
     * Whether the component is currently considered "dirty", i.e whether it needs to be resized or redrawn.
     */
    protected boolean dirty = false;

    /**
     * The constructor.
     *
     * @param config The ModConf implementation that is used by this screen.
     */
    public ModConfScreen(ModConfSpec config) {
        this.config = config;
    }

    @Override
    public int getInspectorWidth() {
        return 600;
    }

    @Override
    public ArrayList<DialogComponent> getItems() {
        ArrayList<DialogComponent> alist = new ArrayList<>();
        for (ConfigurationSection section : config.getSections()) {
            for (ConfigurationOption<?> option : section.getChildren()) {
                if (option instanceof BooleanOption) {
                    alist.add(new NamedCheckBoxComponent((BooleanOption) option));
                } else if (option instanceof NumberOption<?>) {
                    alist.add(new NamedIntegerChooserComponent(this, (NumberOption<?>) option));
                } else if (option instanceof StringOption) {
                    alist.add(new NamedStringChooserComponent(this, (StringOption) option));
                } else {
                    ModConfScreen.LOGGER.error("Unsupported option of class: " + option.getClass().getName());
                }
            }
        }
        this.dirty = false;
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
        return !this.dirty;
    }

    public void markDirty() {
        this.dirty = true;
    }
}
