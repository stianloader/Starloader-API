package de.geolykt.starloader.impl.text;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextComponent;
import de.geolykt.starloader.impl.util.PseudoImmutableArrayList;

@Deprecated(forRemoval = true, since = "2.0.0")
public class BaseFormattedTextComponent implements FormattedTextComponent {

    protected final @NotNull PseudoImmutableArrayList<@NotNull TextComponent> components;
    protected final @NotNull String text;

    public BaseFormattedTextComponent(@NotNull TextComponent mainComponent, @NotNull TextComponent... components) {
        this.components = new PseudoImmutableArrayList<>(components.length + 1);
        for (TextComponent component : components) {
            this.components.unsafeAdd(component);
        }
        this.components.unsafeAdd(mainComponent);
        this.text = mainComponent.getText();
    }

    public BaseFormattedTextComponent(@NotNull TextComponent mainComponent, @NotNull List<@NotNull TextComponent> components) {
        this.components = new PseudoImmutableArrayList<>(components.size() + 1);
        this.components.unsafeAddAll(components);
        this.components.unsafeAdd(mainComponent);
        this.text = mainComponent.getText();
    }

    @Override
    public @NotNull List<@NotNull TextComponent> getComponents() {
        return components;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }
}
