package de.geolykt.starloader.api.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;

/**
 * The {@link KeystrokeInputHandler} controls the state of {@link Keybind Keybinds} and
 * represents the middle man between the GDX {@link InputProcessor} and {@link Keybind#executeAction()}.
 *
 * @since 2.0.0
 */
public class KeystrokeInputHandler {

    private final static class KeybindEntry {
        private boolean down;
        @NotNull
        private final Keybind keybind;
        private int @NotNull[] requiredKeystrokes;

        public KeybindEntry(@NotNull Keybind keybind, int @NotNull... requiredKeystrokes) {
            this.down = false;
            this.keybind = keybind;
            this.requiredKeystrokes = requiredKeystrokes;
        }
    }

    @NotNull
    private static final KeystrokeInputHandler INSTANCE = new KeystrokeInputHandler();

    /**
     * Obtains the currently active instance of the {@link KeystrokeInputHandler}.
     *
     * @return The active instance
     * @since 2.0.0
     */
    @NotNull
    public static final KeystrokeInputHandler getInstance() {
        return KeystrokeInputHandler.INSTANCE;
    }

    @NotNull
    private final List<KeybindEntry> entries = new CopyOnWriteArrayList<>();

    @NotNull
    private final AtomicInteger pressedKeyCount = new AtomicInteger();

    private final int @NotNull[] pressedKeys = new int[4096];

    /**
     * Obtains the currently registered keybinds.
     * The returned collection is independent from the actual registered keybinds after
     * this method is invoked.
     *
     * @return A snapshot of the currently registered keybinds
     * @since 2.0.0
     */
    @NotNull
    public Collection<Keybind> getKeybinds() {
        List<Keybind> keybinds = new ArrayList<>();
        for (KeybindEntry entry : this.entries) {
            keybinds.add(entry.keybind);
        }
        return keybinds;
    }

    /**
     * Obtain the keys that need to be pressed as an array of scancodes as per the {@link Keys} class.
     *
     * <p>Before you attempt any seriously nefarious doings beware that a clone of the internal arrays
     * is returned. No poking at internals is allowed in order to allow future performance improvements.
     * If changing the required scancodes is wanted, use {@link #registerKeybind(Keybind, int[])} instead.
     *
     * @param key The namespaced ID of the Keybind, as per {@link Keybind#getID()}.
     * @return An array of the scancodes that need to be pressed for the given keybind to take effect.
     * @throws IllegalStateException If there is no keybind under the given ID.
     * @since 2.0.0
     */
    public int @NotNull [] getRequiredScancodes(@NotNull NamespacedKey key) {
        for (KeybindEntry entry : this.entries) {
            if (entry.keybind.getID().equals(key)) {
                return entry.requiredKeystrokes;
            }
        }
        throw new IllegalStateException("No keybind registered under given ID: " + key);
    }

    /**
     * Inform the keystroke input handler that a keystroke has been performed.
     *
     * <p>This method must be invoked in the main input logic thread. Failure to do so may cause horrendous
     * issues when paired with other mods.
     *
     * <p>This method does nothing if the key is already pressed according to this handler.
     *
     * @param scancode The GDX scancode of the key that was pressed
     * @since 2.0.0
     * @see Keys
     */
    public void onKeyPress(int scancode) {
        final int keysPressed = this.pressedKeyCount.get();
        for (int i = 0; i < keysPressed; i++) {
            if (this.pressedKeys[i] == scancode) {
                return; // Are we seeing phantoms? Whatever.
            }
        }

        if (!this.pressedKeyCount.compareAndSet(keysPressed, keysPressed + 1)) {
            Galimulator.panic("Race condition detected: The keystroke input handler is not multi-threadable. Avoid calling any of it's methods outside the LWJGL input thread. This is definetly mod-caused and although this error occurs sporadically it should be reported to the respective mod authors.", true);
            return;
        }

        this.pressedKeys[keysPressed] = scancode;

        KeybindEntry selectedKeybind = null;
        int keybindKeyCount = 0;
        loop1:
        for (KeybindEntry entry : this.entries) {
            if (entry.down || entry.requiredKeystrokes.length < keybindKeyCount) {
                continue;
            }

            // Ensure that the keybind contains the key that was just pressed
            // as some edge-cases exist with multi-key keybinds due to the trigger-once behaviour.
            block2: {
                for (int key : entry.requiredKeystrokes) {
                    if (key == scancode) {
                        break block2;
                    }
                }
                continue loop1;
            }

            // Verify that all required keys have been pressed - otherwise go to the next keybind.
            loop2:
            for (int key : entry.requiredKeystrokes) {
                if (key == scancode) {
                    continue;
                }
                for (int i = 0; i < keysPressed; i++) {
                    if (this.pressedKeys[i] == key) {
                        continue loop2;
                    }
                }
                continue loop1;
            }

            if (entry.requiredKeystrokes.length != keybindKeyCount) {
                keybindKeyCount = entry.requiredKeystrokes.length;
                selectedKeybind = entry;
            } else {
                assert selectedKeybind != null; // When this piece of code is being executed, selectedKeybind will be != null

                loop2:
                for (int i = keysPressed - 1; i >= 0; i--) {
                    int witness = this.pressedKeys[i];
                    boolean contains = false;
                    for (int key : entry.requiredKeystrokes) {
                        if (key == witness) {
                            contains = true;
                            break;
                        }
                    }
                    for (int key : selectedKeybind.requiredKeystrokes) {
                        if (key == witness) {
                            if (contains) {
                                continue loop2;
                            } else {
                                LoggerFactory.getLogger(KeystrokeInputHandler.class).warn("Keybind collision betwen {} and {}. Latter will be executed.", entry.keybind.getID(), selectedKeybind.keybind.getID());
                                break loop2;
                            }
                        }
                    }
                    if (contains) {
                        selectedKeybind = entry;
                        break loop2;
                    }
                }
            }
        }

        if (selectedKeybind != null) {
            selectedKeybind.down = true;
            selectedKeybind.keybind.executeAction();
        }
    }

    /**
     * Inform the keystroke input handler that a key is no longer being held down and as
     * such plays no role in keybind handling.
     *
     * <p>This method must be invoked in the main input logic thread. Failure to do so may cause
     * severe race conditions.
     *
     * @param scancode The GDX scancode of the key that was released
     * @since 2.0.0
     * @see Keys
     */
    public void onKeyRelease(int scancode) {
        int keysPressed = this.pressedKeyCount.get();
        int searchIndex = -1;
        for (int i = 0; i < keysPressed; i++) {
            if (this.pressedKeys[i] == scancode) {
                searchIndex = i;
                break;
            }
        }

        if (searchIndex == -1) {
            // Seeing phantoms - whatever.
            return;
        }

        if (!this.pressedKeyCount.compareAndSet(keysPressed, --keysPressed)) {
            Galimulator.panic("Race condition detected: The keystroke input handler is not multi-threadable. Avoid calling any of it's methods outside the LWJGL input thread. This is definetly mod-caused and although this error occurs sporadically it should be reported to the respective mod authors.", true);
            return;
        }

        // We might be able to use SIMD here, but alas most galimulator instances probably run on Java 8.
        // However the Vector API was implemented in Java 18 and will be public with Java 21.
        // Perhaps this is something to get excited for the future? But until then this piece of code is either
        // removed from the codebase or forgotten to history in a forest of random code.
        // I do wonder how large the codebase will be at this point - if we get to this point,
        // after all I [geolykt] will be in uni at this point in time! Sure, development could in theory still continue
        // but I am quite sure that noone will continue this project without me and I should start
        // being more serious about my life at this point.
        // To be honest, it harrows me to think that not even a year is left until the mayhem starts.
        //   - and with that the galimulator modding would end;
        // perhaps even the galimulator community as a whole. After all I don't expect anyone besides Estrect being
        // capable of holding it up. And even with the combined strength of Estrect it probably will not suffice.

        for (int i = searchIndex; i < keysPressed; i++) {
            this.pressedKeys[i] = this.pressedKeys[i + 1];
        }

        for (KeybindEntry entry : this.entries) {
            if (!entry.down) {
                continue;
            }
            for (int key : entry.requiredKeystrokes) {
                if (key == scancode) {
                    entry.down = false;
                    break;
                }
            }
        }
    }

    /**
     * Register a keybind to the handler so the handler is capable of capturing inputs to it.
     *
     * <p>If the keybind is already registered, the keybind is reset.
     *
     * @param keybind The keybind instance to register
     * @param requiredKeystrokes The scancodes that need to be pressed for the keybind to fire
     * @since 2.0.0
     * @see Keys
     */
    public void registerKeybind(@NotNull Keybind keybind, int @NotNull... requiredKeystrokes) {
        this.unregisterKeybind(keybind.getID());
        this.entries.add(new KeybindEntry(keybind, requiredKeystrokes));
    }

    /**
     * Unregister the keybind that is defined by the {@link NamespacedKey}.
     *
     * @param key The ID of the keybind as per {@link Keybind#getID()}.
     * @return True if succeeded, false otherwise
     * @since 2.0.0
     */
    public boolean unregisterKeybind(@NotNull NamespacedKey key) {
        Iterator<KeybindEntry> it = this.entries.iterator();
        while (it.hasNext()) {
            if (it.next().keybind.getID().equals(key)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
