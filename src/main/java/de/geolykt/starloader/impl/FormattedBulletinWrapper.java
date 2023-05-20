package de.geolykt.starloader.impl;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.text.FormattedText;

import snoddasmannen.galimulator.TextBulletin;

@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class FormattedBulletinWrapper extends TextBulletin {

    private final FormattedText text;

    public FormattedBulletinWrapper(FormattedText text) {
        super(text.getText());
        this.text = text;
    }

    @Override
    public void drawAt(float x, float y) {
        text.renderText(x, y);
    }
}
