package de.geolykt.starloader.impl;

import de.geolykt.starloader.api.gui.text.FormattedText;

import snoddasmannen.galimulator.class_42;

public class FormattedBulletinWrapper extends class_42 {

    private final FormattedText text;

    public FormattedBulletinWrapper(FormattedText text) {
        super(text.getText());
        this.text = text;
    }

    public void a(float var1, float var2) {
        text.renderText(var1, var2);
    }
}
