package de.geolykt.starloader.impl.text;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;

@Deprecated(forRemoval = true, since = "2.0.0")
public class BaseFormattedText implements FormattedText {

    private final @NotNull List<@NotNull FormattedTextComponent> components;
    private final @NotNull String text;

    public BaseFormattedText(@NotNull List<@NotNull FormattedTextComponent> components) {
        this.components = components;
        StringBuilder textBuilder = new StringBuilder();
        for (FormattedTextComponent component : this.components) {
            textBuilder.append(component.getText());
        }
        this.text = NullUtils.requireNotNull(textBuilder.toString());
    }

    public BaseFormattedText(@NotNull FormattedTextComponent... components2) {
        this(NullUtils.requireNotNull(Arrays.asList(components2)));
    }

    @Override
    public @NotNull List<@NotNull FormattedTextComponent> getComponents() {
        return components;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }
}
