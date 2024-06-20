package de.geolykt.starloader.impl.gui;

import java.util.List;

import snoddasmannen.galimulator.ui.OptionChooserWidget;

public class SLOptionChooserWidget extends OptionChooserWidget {

    private boolean optionSelected = false;

    public SLOptionChooserWidget(String title, String description, List<?> choices, int duration, WIDGET_ID wID) {
        super(title, description, choices, duration, wID);
    }

    @Override
    public void a(Object object) {
        this.optionSelected = true;
        super.a(object);
        this.optionSelected = false;
    }

    public boolean hasSelectedOption() {
        return this.optionSelected;
    }
}
