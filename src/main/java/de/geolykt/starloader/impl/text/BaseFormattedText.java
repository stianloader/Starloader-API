package de.geolykt.starloader.impl.text;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;

public class BaseFormattedText implements FormattedText {

    private final List<FormattedTextComponent> components;
    private final String text;

    public BaseFormattedText(List<FormattedTextComponent> components) {
        this.components = components;
        StringBuilder textBuilder = new StringBuilder();
        for (FormattedTextComponent component : this.components) {
            textBuilder.append(component.getText());
        }
        this.text = textBuilder.toString();
    }

    public BaseFormattedText(FormattedTextComponent... components2) {
        this(Arrays.asList(components2));
    }

    @Override
    public @NotNull List<FormattedTextComponent> getComponents() {
        return components;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }
}
