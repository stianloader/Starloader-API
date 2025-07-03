package de.geolykt.starloader.impl.gui.rendercache;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.api.gui.rendercache.RenderObject;

import snoddasmannen.galimulator.rendersystem.RenderItem;

public class RunnableRenderObject extends RenderItem {

    @NotNull
    private final Runnable renderAction;

    public RunnableRenderObject(@NotNull Runnable renderAction, @NotNull Rectangle boundingBox, @NotNull OrthographicCamera camera) {
        this.renderAction = Objects.requireNonNull(renderAction, "renderAction may not be null");
        ((RenderObject) this).setCamera(camera);
        ((RenderObject) this).setAABB(boundingBox);
    }

    @Override
    public void a() {
        this.renderAction.run();
    }
}
