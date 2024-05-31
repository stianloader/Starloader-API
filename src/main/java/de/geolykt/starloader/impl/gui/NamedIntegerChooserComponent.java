package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.TextInputBuilder;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerChooseOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.gui.modconf.NumberOption;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.dialog.LabeledStringChooserComponent;

public class NamedIntegerChooserComponent extends LabeledStringChooserComponent {

    protected static Vector<@NotNull Object> getOptions(@NotNull NumberOption<?> option) {
        final Vector<@NotNull Object> options = new Vector<>(option.getRecommendedValues());
        if (!(option instanceof IntegerChooseOption)) {
            options.add("Custom");
        }
        return options;
    }

    @NotNull
    protected final NumberOption<? extends Number> option;

    @NotNull
    protected final ModConfScreen parent;

    public NamedIntegerChooserComponent(@NotNull ModConfScreen parent, @NotNull NumberOption<? extends Number> option) {
        // name / currentValue / options / category
        super(option.getName(), option.get().toString(), getOptions(option), option.getParent().getName());
        this.parent = NullUtils.requireNotNull(parent);
        this.option = option;
    }

    @Override
    public void a(final String o) {
        if ("Custom".equals(o)) {
            TextInputBuilder builder = Drawing.textInputBuilder("Change value of setting", NullUtils.requireNotNull(this.option.get().toString()), this.option.getName());
            builder.addHook(text -> {
                if (text == null) {
                    return; // User cancelled input
                }
                Double d;
                try {
                    d = Double.valueOf(text);
                } catch (NumberFormatException e) {
                    Drawing.toast(NullUtils.format("%s is not a valid number.", text));
                    return;
                }
                if (d < this.option.getMinimum().doubleValue()) {
                    Drawing.toast(NullUtils.format("%s is below the minimum value of %s", text, this.option.getMinimum().toString()));
                    return;
                }
                if (d > this.option.getMaximum().doubleValue()) {
                    Drawing.toast(NullUtils.format("%s is above the maximum value of %s", text, this.option.getMaximum().toString()));
                    return;
                }
                NumberOption<?> rawOpt = this.option; // We have to trick the compiler in order to get this to compile.
                if (rawOpt instanceof FloatOption) {
                    ((FloatOption) rawOpt).setValue(d.floatValue());
                    this.getParentScreen().markDirty();
                } else if (rawOpt instanceof IntegerOption) {
                    ((IntegerOption) rawOpt).setValue(d.intValue());
                    this.getParentScreen().markDirty();
                } else {
                    @SuppressWarnings("unchecked")
                    NumberOption<Number> rawr = (NumberOption<Number>) this.option;
                    rawr.set(d);
                    this.getParentScreen().markDirty();
                }
            });
            builder.build();
            return;
        }
        try {
            if (this.option instanceof FloatOption) {
                ((FloatOption) this.option).setValue(Float.valueOf(o));
            } else if (option instanceof IntegerOption) {
                ((IntegerOption) this.option).setValue(Integer.valueOf(o));
            } else {
                @SuppressWarnings("unchecked")
                NumberOption<Number> rawr = (NumberOption<Number>) this.option;
                Integer i = Integer.decode(o);
                if (i == null) {
                    throw new NullPointerException();
                }
                rawr.set(i);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Drawing.toast("Internal error! Please view the logs for more detail");
            AudioSampleWrapper.UI_ERROR.play();
        }
    }

    @NotNull
    public ModConfScreen getParentScreen() {
        return this.parent;
    }
}
