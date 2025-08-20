package de.geolykt.starloader.impl.usertest;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasPosition;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.prefab.AbstractConsoleCanvasContext;

class CanvasConsoleTest extends Usertest {
    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }

    @Override
    @NotNull
    public String getName() {
        return "CanvasConsole";
    }

    @Override
    public void runTest() {
        CanvasManager canvasManager = CanvasManager.getInstance();
        Canvas canvas = canvasManager.childCanvas(new AbstractConsoleCanvasContext(800, 600, Drawing.getSpaceFont()) {
            @NotNull
            private final List<@NotNull String> lines = new ArrayList<>();

            {
                this.lines.add("[RED]CanvasConsoleTest Console. Press [ESCAPE] to exit.");
            }

            @Override
            @Nullable
            public String getLine(int lineIndex) {
                if (lineIndex < this.lines.size()) {
                    return this.lines.get(lineIndex);
                }
                return null;
            }

            @Override
            public void processInput(@NotNull String entered) {
                this.lines.add(0, entered);
            }
        });
        canvas = canvasManager.withMargins(5, 5, 5, 5, canvas, new CanvasSettings("CanvasConsoleTest"));
        canvasManager.openCanvas(canvas, CanvasPosition.CENTER);
    }
}
