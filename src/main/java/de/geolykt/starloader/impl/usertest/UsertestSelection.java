package de.geolykt.starloader.impl.usertest;

import java.util.ArrayList;

import snoddasmannen.galimulator.Dialog;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.dialog.DialogButton;
import snoddasmannen.galimulator.dialog.DialogComponent;
import snoddasmannen.galimulator.ui.Widget.WIDGET_ID;
import snoddasmannen.galimulator.ui.class_47;

public class UsertestSelection extends DialogButton implements Dialog {

    public UsertestSelection(String name, GalColor c1, GalColor c2, String category) {
        super(name, c1, c2, category);
    }

    @Override
    public ArrayList<DialogComponent> getItems() {
        ArrayList<DialogComponent> components = new ArrayList<>();
        for (Usertest test : Usertest.USERTESTS) {
            DialogButton button = new DialogButton(test.getName(), GalColor.WHITE, GalColor.WHITE, test.getCategoryName()) {
                @Override
                public void onTouch() {
                    try {
                        test.runTest();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            };
            components.add(button);
        }
        return components;
    }

    @Override
    public String getTitle() {
        return "Usertest Selection Dialog";
    }

    @Override
    public GalColor getTitlebarColor() {
        return GalColor.RED;
    }

    @Override
    public int getInspectorWidth() {
        return 800;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void onTouch() {
        // TODO deobfuscate
        class_47 screenWrapper = new class_47(this, true);
        screenWrapper.a((WIDGET_ID) null);
        Space.showWidget(screenWrapper);
    }
}
