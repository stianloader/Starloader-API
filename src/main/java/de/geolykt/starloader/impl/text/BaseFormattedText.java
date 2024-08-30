package de.geolykt.starloader.impl.text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;

@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class BaseFormattedText implements FormattedText {

    @NotNull
    private final List<@NotNull FormattedTextComponent> components;
    @NotNull
    private final String text;

    public BaseFormattedText(@NotNull List<@NotNull FormattedTextComponent> components) {
        this.components = components;
        StringBuilder textBuilder = new StringBuilder();
        for (FormattedTextComponent component : this.components) {
            textBuilder.append(component.getText());
        }
        this.text = Objects.requireNonNull(textBuilder.toString());
    }

    public BaseFormattedText(@NotNull FormattedTextComponent... components2) {
        this(Objects.requireNonNull(Arrays.asList(components2)));
    }

    @Override
    @NotNull
    public List<@NotNull FormattedTextComponent> getComponents() {
        return this.components;
    }

    @Override
    @NotNull
    public String getText() {
        return this.text;
    }
}
