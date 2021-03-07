package de.geolykt.starloader.api.gui;

/**
 * Listens for {@link WidgetAction} on Widgets the listener has been registered to.
 */
@FunctionalInterface
public interface WidgetActionListener {
    public void onAction(WidgetAction action);
}
