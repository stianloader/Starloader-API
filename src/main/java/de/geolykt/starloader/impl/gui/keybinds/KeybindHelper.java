package de.geolykt.starloader.impl.gui.keybinds;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import de.geolykt.starloader.StarloaderAPIExtension;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.KeystrokeInputHandler;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.MapMode;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.ModUploadWidget;
import snoddasmannen.galimulator.ui.SidebarWidget;

public class KeybindHelper {

    @NotNull
    public static final NamespacedKey ZOOM_IN = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_zoom_in");

    @NotNull
    public static final NamespacedKey ZOOM_OUT = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_zoom_out");

    @NotNull
    public static final NamespacedKey CENTER_CAMERA = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_center_camera");

    @NotNull
    public static final NamespacedKey RESET_SCREEN = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_reset_screen");

    @NotNull
    public static final NamespacedKey CLOSE_LAST_WINDOW = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_close_last_window");

    @NotNull
    public static final NamespacedKey TOGGLE_UI = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_toggle_ui");

    @NotNull
    public static final NamespacedKey TAKE_SCREENSHOT = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_screenshot");

    @NotNull
    public static final NamespacedKey MOVE_LEFT = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_move_left");

    @NotNull
    public static final NamespacedKey MOVE_RIGHT = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_move_right");

    @NotNull
    public static final NamespacedKey MOVE_UP = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_move_up");

    @NotNull
    public static final NamespacedKey MOVE_DOWN = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_move_down");

    @NotNull
    public static final NamespacedKey FULLSCREEN = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_fullscreen");

    @NotNull
    public static final NamespacedKey UPLOAD_MODDED_ITEMS = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_upload_modded_items");

    @NotNull
    public static final NamespacedKey OPEN_WAR_ROOM = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_open_war_room");

    @NotNull
    public static final NamespacedKey ALLIANCE_MAP_MODE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_map_mode_alliance");

    @NotNull
    public static final NamespacedKey RELIGION_MAP_MODE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_map_mode_religion");

    @NotNull
    public static final NamespacedKey WEALTH_MAP_MODE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_map_mode_wealth");

    @NotNull
    public static final NamespacedKey HEAT_MAP_MODE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_map_mode_heat");

    @NotNull
    public static final NamespacedKey OPEN_PROFILER = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_open_profiler");

    @NotNull
    public static final NamespacedKey ROTATE_CLOCKWISE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_rotate_clockwise");

    @NotNull
    public static final NamespacedKey ROTATE_ANTICLOCKWISE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_rotate_anticlockwise");

    @NotNull
    public static final NamespacedKey PAUSE = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_pause");

    @NotNull
    public static final NamespacedKey STEP_100 = new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_step_100");

    public static void registerAll(@NotNull KeystrokeInputHandler handler) {
        int oldCount = handler.getKeybinds().size();
        handler.registerKeybind(new KeybindZoom("Zoom out", ZOOM_OUT, 1.1F), new int[]{Keys.X});
        handler.registerKeybind(new KeybindZoom("Zoom in", ZOOM_IN, 0.9F), new int[]{Keys.Z});
        handler.registerKeybind(new LambdaKeybind("Center camera", CENTER_CAMERA, GalFX::n), new int[]{Keys.C});
        handler.registerKeybind(new LambdaKeybind("Reset screen", RESET_SCREEN, () -> {
            Gdx.graphics.setWindowedMode(1280, 720);
        }), new int[]{Keys.Q});
        handler.registerKeybind(new LambdaKeybind("Close last window", CLOSE_LAST_WINDOW, () -> {
            boolean var1 = Space.C();
            if (!var1) {
                SidebarWidget.openGameControl();
            }
        }), new int[]{Keys.ESCAPE});
        handler.registerKeybind(new LambdaKeybind("Reset screen", TOGGLE_UI, Space::ag), new int[]{Keys.F1});
        handler.registerKeybind(new LambdaKeybind("Take screenshot", TAKE_SCREENSHOT, () -> {
            // TODO reimplement (it'd be boring if we just copy & paste the galimulator implementation).
        }), new int[]{Keys.F2});
        handler.registerKeybind(new KeybindMove("Scroll left", MOVE_LEFT, -50F, 0F), new int[] {Keys.LEFT});
        handler.registerKeybind(new KeybindMove("Scroll right", MOVE_RIGHT, 50F, 0F), new int[] {Keys.RIGHT});
        handler.registerKeybind(new KeybindMove("Scroll up", MOVE_UP, 0F, 50F), new int[] {Keys.UP});
        handler.registerKeybind(new KeybindMove("Scroll down", MOVE_DOWN, 0F, -50F), new int[] {Keys.DOWN});
        handler.registerKeybind(new LambdaKeybind("Toggle fullscreen", FULLSCREEN, GalFX::o), new int[]{Keys.F});
        handler.registerKeybind(new LambdaKeybind("Upload modded items", UPLOAD_MODDED_ITEMS, () -> {
            Space.showWidget(ModUploadWidget.class);
        }), new int[]{Keys.M});
        handler.registerKeybind(new LambdaKeybind("Open war room", OPEN_WAR_ROOM, () -> {
            if (Space.getPlayer().b()) {
                Space.Z();
            }
        }), new int[]{Keys.O});
        handler.registerKeybind(new LambdaKeybind("Toggle alliance map mode", ALLIANCE_MAP_MODE, () -> {
            MapMode.setCurrentMode(MapMode.getCurrentMode() == MapMode.MapModes.NORMAL ? MapMode.MapModes.ALLIANCES : MapMode.MapModes.NORMAL);
        }), new int[]{Keys.A});
        handler.registerKeybind(new LambdaKeybind("Toggle religion map mode", RELIGION_MAP_MODE, () -> {
            MapMode.setCurrentMode(MapMode.getCurrentMode() == MapMode.MapModes.NORMAL ? MapMode.MapModes.RELIGION : MapMode.MapModes.NORMAL);
        }), new int[]{Keys.R});
        handler.registerKeybind(new LambdaKeybind("Toggle wealth map mode", WEALTH_MAP_MODE, () -> {
            MapMode.setCurrentMode(MapMode.getCurrentMode() == MapMode.MapModes.NORMAL ? MapMode.MapModes.WEALTH : MapMode.MapModes.NORMAL);
        }), new int[]{Keys.W});
        handler.registerKeybind(new LambdaKeybind("Toggle heat map mode", HEAT_MAP_MODE, () -> {
            MapMode.setCurrentMode(MapMode.getCurrentMode() == MapMode.MapModes.NORMAL ? MapMode.MapModes.HEAT : MapMode.MapModes.NORMAL);
        }), new int[]{Keys.H});
        handler.registerKeybind(new LambdaKeybind("Show profiler data", OPEN_PROFILER, Space::ac), new int[]{Keys.P});
        // Usage of Y & U is a rather strange descision due to QWERTZ (it makes sense on QWERTY keyboards though)
        handler.registerKeybind(new KeybindRotate("Rotate the galaxy clockwise", ROTATE_CLOCKWISE, 45F), new int[] {Keys.Y});
        handler.registerKeybind(new KeybindRotate("Rotate the galaxy counter-clockwise", ROTATE_ANTICLOCKWISE, -45F), new int[] {Keys.U});
        handler.registerKeybind(new LambdaKeybind("Pause & Unpause the game", PAUSE, () -> {
            Galimulator.setPaused(!Galimulator.isPaused());
        }), new int[]{Keys.SPACE});
        handler.registerKeybind(new LambdaKeybind("Step forward 100 steps", STEP_100, () -> {
            Space.h(100);
        }), new int[]{Keys.S});
        for (int i = 1; i < 9; i++) {
            handler.registerKeybind(new KeybindSetTimelapseModifier("Set Timelapse Modifier (" + (1 << (i - 1)) + "x)", new NamespacedKey(StarloaderAPIExtension.getInstance(), "keybind_timelapse_" + (i - 1)), i - 1), new int[] {Keys.NUMPAD_0 + i});
        }

        LoggerFactory.getLogger(KeybindHelper.class).info("Registered {} new keybinds.", handler.getKeybinds().size() - oldCount);
    }
}
