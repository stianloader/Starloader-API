package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.prefab.AbstractTextCanvasContext;
import de.geolykt.starloader.api.gui.canvas.prefab.RunnableCanvasButton;
import de.geolykt.starloader.impl.gui.canvas.VolatileDummyContext;

public class TPSOverrideTest extends Usertest {

    @Override
    public void runTest() {
        CanvasManager cmgr = CanvasManager.getInstance();

        CanvasContext ctx1 = new AbstractTextCanvasContext(Drawing.getSpaceFont()) {
            @Override
            @NotNull
            public CharSequence getText() {
                return "Current TPS target: [GRAY]" + Galimulator.getConfiguration().getTargetTPS() + "[]";
            }
        };

        CanvasContext ctx2 = new AbstractTextCanvasContext(Drawing.getSpaceFont()) {
            @Override
            @NotNull
            public CharSequence getText() {
                return "Current ticks per frame: [GRAY]" + Galimulator.getConfiguration().getTimelapseModifier() + "[]";
            }
        };

        Canvas targetTpsRow = cmgr.multiCanvas(new VolatileDummyContext(() -> {
            return ctx1.getWidth() + 200;
        }, 50), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.LEFT_TO_RIGHT, new RunnableCanvasButton(() -> {
            // Set TPS
            Drawing.textInputBuilder("Set ticks per second", "", "").addHook((text) -> {
                try {
                    Galimulator.getConfiguration().setTargetTPS(Integer.parseInt(text));
                } catch (NumberFormatException nfe) {
                    Drawing.toast("Did not enter a valid number! The TPS must be a positive whole number.");
                }
            }).build();
        }, "Set ticks per second", 200, 50), ctx1);

        Canvas fixedTPFRow = cmgr.multiCanvas(new VolatileDummyContext(() -> {
            return ctx2.getWidth() + 200;
        }, 50), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.LEFT_TO_RIGHT, new RunnableCanvasButton(() -> {
            // Set TPF
            Drawing.textInputBuilder("Set ticks per frame", "", "").addHook((text) -> {
                try {
                    Galimulator.getConfiguration().setTimelapseModifier(Integer.parseUnsignedInt(text));
                } catch (NumberFormatException nfe) {
                    Drawing.toast("Did not enter a valid number! The ticks per frame must be a positive whole number.");
                }
            }).build();
        }, "Set ticks per frame", 200, 50), ctx2);

        Canvas completeCanvas = cmgr.multiCanvas(new VolatileDummyContext(() -> {
            return Math.max(ctx1.getWidth(), ctx2.getWidth()) + 200;
        }, 100), new CanvasSettings("TPS Overrides"), ChildObjectOrientation.BOTTOM_TO_TOP, targetTpsRow, fixedTPFRow);

        cmgr.openCanvas(completeCanvas);
    }

    @Override
    @NotNull
    public String getName() {
        return "TPS Override";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }
}
