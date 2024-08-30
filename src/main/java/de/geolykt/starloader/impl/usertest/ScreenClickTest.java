package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.ReactiveComponent;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenBuilder;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

public class ScreenClickTest extends Usertest{

    private static class ReactiveClickTestComponent implements ScreenComponent, ReactiveComponent {
        @NotNull
        private final Screen s;
        private float lastX;
        private float lastY;

        public ReactiveClickTestComponent(@NotNull Screen s) {
            this.s = s;
        }

        @Override
        public int renderAt(float x, float y, @NotNull Camera camera) {
            Drawing.getDrawingBatch().draw(Drawing.getTextureProvider().getSinglePixelSquare(), x + this.lastX - 5, y + this.lastY - 5, 10, 10);
            return this.getWidth();
        }

        @Override
        public boolean isSameType(@NotNull ScreenComponent component) {
            return false;
        }

        @Override
        public int getWidth() {
            return 400;
        }

        @Override
        @NotNull
        public Screen getParentScreen() {
            return this.s;
        }

        @Override
        @NotNull
        public LineWrappingInfo getLineWrappingInfo() {
            return LineWrappingInfo.alwaysWrapping();
        }

        @Override
        public int getHeight() {
            return 800;
        }

        @Override
        public void onClick(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera) {
            this.lastX = screenX;
            this.lastY = screenY;
        }

        @Override
        public void onHover(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera) {
            // NOP
        }

        @Override
        public void onLongClick(int screenX, int screenY, int componentX, int componentY,
                @NotNull Camera camera) {
            // NOP
        }

        @Override
        public void onScroll(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera,
                int amount) {
            // NOP
        }
    }

    @Override
    public void runTest() {
        Screen s = ScreenBuilder.getBuilder()
                .withHeaderColor(Color.GOLDENROD)
                .withTitle("Screen Click Test")
                .build();
        s.addChild(new ReactiveClickTestComponent(s));
        Drawing.showScreen(s);
    }

    @Override
    @NotNull
    public String getName() {
        return "ScreenClick";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }
}
