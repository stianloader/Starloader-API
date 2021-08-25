package de.geolykt.starloader.impl.gui;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerChooseOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.gui.modconf.NumberOption;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.hl;

public class NamedIntegerChooserComponent extends hl implements ScreenComponent {

    protected static Vector<@NotNull Object> getOptions(@NotNull NumberOption<?> option) {
        final Vector<@NotNull Object> options = new Vector<>(option.getRecommendedValues());
        if (!(option instanceof IntegerChooseOption)) {
            options.add("Custom");
        }
        return options;
    }
    protected final @NotNull NumberOption<? extends Number> option;

    protected final @NotNull Screen parent;

    public NamedIntegerChooserComponent(@NotNull Screen parent, @NotNull NumberOption<? extends Number> option) {
        // Irrelevant  / name / currentValue / options / category / Irrelevant
        super(null, option.getName(), option.get().toString(), getOptions(option), option.getParent().getName(), null);
        this.parent = NullUtils.requireNotNull(parent);
        this.option = option;
    }

    public void a(final String o) {
        if ("Custom".equals(o)) {
            var builder = Drawing.textInputBuilder("Change value of setting", NullUtils.requireNotNull(option.get().toString()), option.getName());
            builder.addHook(text -> {
                Double d;
                try {
                    d = Double.valueOf(text);
                } catch (NumberFormatException e) {
                    Drawing.toast(NullUtils.format("%s is not a valid number.", text));
                    return;
                }
                if (d < option.getMinimum().doubleValue()) {
                    Drawing.toast(NullUtils.format("%s is below the minimum value of %s", text, option.getMinimum().toString()));
                    return;
                }
                if (d > option.getMaximum().doubleValue()) {
                    Drawing.toast(NullUtils.format("%s is above the maximum value of %s", text, option.getMaximum().toString()));
                    return;
                }
                NumberOption<?> rawOpt = option; // We have to trick the compiler in order to get this to compile.
                if (rawOpt instanceof FloatOption) {
                    ((FloatOption) rawOpt).setValue(d.floatValue());
                    getParentScreen().markDirty();
                } else if (rawOpt instanceof IntegerOption) {
                    ((IntegerOption) rawOpt).setValue(d.intValue());
                    getParentScreen().markDirty();
                } else {
                    @SuppressWarnings("unchecked")
                    NumberOption<Number> rawr = (NumberOption<Number>) option;
                    rawr.set(d);
                    getParentScreen().markDirty();
                }
            });
            builder.build();
            return;
        }
        try {
            if (option instanceof FloatOption) {
                ((FloatOption) option).setValue(Float.valueOf(o));
            } else if (option instanceof IntegerOption) {
                ((IntegerOption) option).setValue(Integer.valueOf(o));
            } else {
                @SuppressWarnings("unchecked")
                NumberOption<Number> rawr = (NumberOption<Number>) option;
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

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull LineWrappingInfo getLineWrappingInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Screen getParentScreen() {
        return parent;
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSameType(@NotNull ScreenComponent component) {
        return component instanceof NamedIntegerChooserComponent || component instanceof NamedStringChooserComponent;
    }
}
