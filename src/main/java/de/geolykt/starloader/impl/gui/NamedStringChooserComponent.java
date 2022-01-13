package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.modconf.StrictStringOption;
import de.geolykt.starloader.api.gui.modconf.StringChooseOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.ppclass_169;

public class NamedStringChooserComponent extends ppclass_169 {

    protected static Vector<@NotNull Object> getOptions(StringOption option) {
        final Vector<@NotNull Object> options = new Vector<>(option.getRecommendedValues());
        if (!(option instanceof StrictStringOption)) {
            options.add("Custom");
        }
        return options;
    }
    protected final @NotNull StringOption option;

    protected final @NotNull ModConfScreen parent;

    public NamedStringChooserComponent(@NotNull ModConfScreen parent, @NotNull StringOption option) {
        // Irrelevant  / name / currentValue / options / category / Irrelevant
        super(null, option.getName(), option.get(), getOptions(option), option.getParent().getName(), null);
        this.parent = NullUtils.requireNotNull(parent);
        this.option = option;
    }

    public void a(final String o) {
        if ("Custom".equals(o.toString()) && !(option instanceof StringChooseOption)) {
            var builder = Drawing.textInputBuilder("Change value of setting", option.get(), option.getName());
            if (option instanceof StrictStringOption) {
                builder.addHook(text -> {
                    if (text == null) {
                        return; // cancelled
                    }
                    if (((StrictStringOption) option).isValid(text)) {
                        option.set(text);
                        getParentScreen().markDirty();
                    } else {
                        Drawing.toast("The input value is not valid!");
                    }
                });
            } else {
                builder.addHook(text -> {
                    if (text == null) {
                        return; // cancelled
                    }
                    option.set(text);
                    getParentScreen().markDirty();
                });
            }
            builder.build();
            return;
        }
        option.set(NullUtils.requireNotNull(o.toString()));
    }

    public @NotNull ModConfScreen getParentScreen() {
        return parent;
    }
}
