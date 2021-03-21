package de.geolykt.starloader.impl.text;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextComponent;
import de.geolykt.starloader.impl.util.PseudoImmutableArrayList;

public class BaseFormattedTextComponent implements FormattedTextComponent {

    protected final PseudoImmutableArrayList<TextComponent> components;
    protected final String text;

    public BaseFormattedTextComponent(TextComponent mainComponent, TextComponent... components) {
        this.components = new PseudoImmutableArrayList<>(components.length + 1);
        for (TextComponent component : components) {
            this.components.unsafeAdd(component);
        }
        this.components.unsafeAdd(mainComponent);
        this.text = mainComponent.getText();
    }

    public BaseFormattedTextComponent(TextComponent mainComponent, List<TextComponent> components) {
        this.components = new PseudoImmutableArrayList<>(components.size() + 1);
        this.components.unsafeAddAll(components);
        this.components.unsafeAdd(mainComponent);
        this.text = mainComponent.getText();
    }

    @Override
    public @NotNull List<TextComponent> getComponents() {
        return components;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }
}
