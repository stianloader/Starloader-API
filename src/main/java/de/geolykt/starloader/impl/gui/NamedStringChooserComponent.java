package de.geolykt.starloader.impl.gui;

import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.TextInputBuilder;
import de.geolykt.starloader.api.gui.modconf.StrictStringOption;
import de.geolykt.starloader.api.gui.modconf.StringChooseOption;
import de.geolykt.starloader.api.gui.modconf.StringOption;

import snoddasmannen.galimulator.dialog.LabeledStringChooserComponent;

public class NamedStringChooserComponent extends LabeledStringChooserComponent {

    protected static Vector<@NotNull Object> getOptions(StringOption option) {
        final Vector<@NotNull Object> options = new Vector<>(option.getRecommendedValues());
        if (!(option instanceof StringChooseOption)) {
            options.add("Custom");
        }
        return options;
    }

    @NotNull
    protected final StringOption option;

    @NotNull
    protected final ModConfScreen parent;

    public NamedStringChooserComponent(@NotNull ModConfScreen parent, @NotNull StringOption option) {
        // name / currentValue / options / category
        super(option.getName(), option.get(), NamedStringChooserComponent.getOptions(option), option.getParent().getName());
        this.parent = Objects.requireNonNull(parent);
        this.option = option;
    }

    @Override
    public void a(final String o) {
        if ("Custom".equals(o.toString()) && !(option instanceof StringChooseOption)) {
            TextInputBuilder builder = Drawing.textInputBuilder("Change value of setting", this.option.get(), this.option.getName());
            if (this.option instanceof StrictStringOption) {
                builder.addHook(text -> {
                    if (text == null) {
                        return; // cancelled
                    }
                    if (((StrictStringOption) this.option).isValid(text)) {
                        this.option.set(text);
                        this.getParentScreen().markDirty();
                    } else {
                        Drawing.toast("The input value is not valid!");
                    }
                });
            } else {
                builder.addHook(text -> {
                    if (text == null) {
                        return; // cancelled
                    }
                    this.option.set(text);
                    this.getParentScreen().markDirty();
                });
            }
            builder.build();
            return;
        }
        this.option.set(Objects.requireNonNull(o.toString()));
    }

    @NotNull
    public ModConfScreen getParentScreen() {
        return this.parent;
    }
}
