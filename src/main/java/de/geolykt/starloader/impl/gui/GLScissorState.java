package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * A wrapper around GL calls that capture the state of the {@link GL20#GL_SCISSOR_BOX}.
 *
 * @since 2.0.0
 */
public final class GLScissorState {

    private static int currentH;
    private static int currentW;
    private static int currentX;
    private static int currentY;
    private static boolean scissorInit;

    @NotNull
    public static GLScissorState captureScissor() {
        if (!scissorInit) {
            scissorInit = true;
            currentX = 0;
            currentY = 0;
            currentW = Gdx.graphics.getBackBufferWidth();
            currentH = Gdx.graphics.getBackBufferHeight();
        }

        boolean scissorEnabled = Gdx.gl.glIsEnabled(GL20.GL_SCISSOR_TEST);
        return new GLScissorState(currentX, currentY, currentW, currentH, scissorEnabled);
    }
    /**
     * Makes the {@link GLScissorState} class "forget" the scissor - which means that the scissor will be at the (0, 0)
     * coordinates with a width and height defined by the BackBuffer.
     *
     * @since 2.0.0
     */
    public static void forgetScissor() {
        GLScissorState.scissorInit = false;
    }
    /**
     * Sets the OpenGL scissor. Calls {@link GL11#glScissor(int, int, int, int)}, but also remembers
     * the arguments it was called with. All {@link GL11#glScissor(int, int, int, int)} calls are delegated
     * to this method.
     *
     * @param x The X-value of the lower left corner of the scissor box
     * @param y The Y-value of the lower left corner of the scissor box
     * @param w The width of the scissor box
     * @param h The height of the scissor box
     * @since 2.0.0
     */
    public static final void glScissor(int x, int y, int w, int h) {
        if (x < 0 || y < 0 || w < 0 || h < 0) {
            throw new IllegalStateException("One of the arguments has a negative value - which is not permitted!");
        }
        GLScissorState.currentX = x;
        GLScissorState.currentY = y;
        GLScissorState.currentW = w;
        GLScissorState.currentH = h;
        GL11.glScissor(x, y, w, h);
    }

    public final boolean enabled;
    public final int height;
    public final int width;
    public final int x;
    public final int y;

    private GLScissorState(int x, int y, int w, int h, boolean enabled) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.enabled = enabled;
    }

    public void reapplyState() {
        Gdx.gl.glScissor(x, y, width, height);
        if (Gdx.gl.glGetError() == GL20.GL_INVALID_VALUE) {
            throw new IllegalStateException("Gdx.gl.glScissor raised an GL20.GL_INVALID_VALUE! Scissor state: " + this.toString());
        }
        if (enabled) {
            if (!Gdx.gl.glIsEnabled(GL20.GL_SCISSOR_TEST)) {
                Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            }
        } else if (Gdx.gl.glIsEnabled(GL20.GL_SCISSOR_TEST)) {
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    @Override
    public String toString() {
        return String.format("GLScissorState[x = %d (0x%X), y = %d (0x%X), w = %d (0x%X), h = %d (0x%X)]", x, x, y, y, width, width, height, height);
    }
}
