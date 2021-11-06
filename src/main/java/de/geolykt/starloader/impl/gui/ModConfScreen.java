package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

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
    public void addChild(@NotNull ScreenComponent o) {
        throw new UnsupportedOperationException("Cannot add new children to this class.");
    }

    @Override
    public boolean canAddChildren() {
        return false;
    }

    @Override
    public @Nullable Camera getCamera() {
        return null;
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
    public int getInnerWidth() {
        return getInspectorWidth() - 20;
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
    public boolean isHeadless() {
        return false;
    }

    @Override
    public boolean isValid() {
        return !dirty;
    }

    @Override
    public Iterator<Entry<Vector2, ScreenComponent>> iterator() {
        throw new UnsupportedOperationException("Not supported - screen contents are calculated on the fly.");
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void setCamera(@NotNull Camera camera) {
        // BITVOID
    }
}
