package de.geolykt.starloader.impl.serial;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.GalaxyLoadingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxyLoadingEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEvent;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.serial.Encoder;
import de.geolykt.starloader.api.serial.MetadataCollector;
import de.geolykt.starloader.api.serial.SavegameFormat;
import de.geolykt.starloader.impl.util.JoiningInputStream;
import de.geolykt.starloader.impl.util.LEB128;

/**
 * A simple wrapper around Vanilla's savegame format that adds savegame metadata as well as slapi metadata.
 * Invocation of any methods of this class outside of the main thread while the main tick loop is not halted
 * may lead to unexpected behaviour.
 * This implementation will automatically fall back to vanilla savegames if the savegame is determined to not be
 * compatible with this format.
 *
 * @since 2.0.0
 */
public class BoilerplateSavegameFormat implements SavegameFormat {

    private static final byte[] FORMAT_HEADER = "SLAPI/0_SAVEGAME".getBytes(StandardCharsets.US_ASCII);
    @NotNull
    public static final BoilerplateSavegameFormat INSTANCE = new BoilerplateSavegameFormat();

    private BoilerplateSavegameFormat() {
        // Prevent the arbitrary creation of instances of this class
    }

    @Override
    @NotNull
    public String getName() {
        return "SLAPI Boilerplate";
    }

    @Override
    public void loadGameState(byte[] data) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            loadGameState(in);
        }
    }

    @Override
    public synchronized void loadGameState(@NotNull InputStream in) throws IOException {
        byte[] header = new byte[FORMAT_HEADER.length];
        if (in.readNBytes(header, 0, header.length) != header.length) {
            throw new IOException("Input stream exhausted prematurely");
        }
        if (!Arrays.equals(FORMAT_HEADER, header)) {
            try {
                // We already read a few bytes, so we have to prepend those bytes to the input stream again
                JoiningInputStream fullIn = new JoiningInputStream(new ByteArrayInputStream(header), in);
                VanillaSavegameFormat.INSTANCE.loadGameState(fullIn);
                return;
            } catch (Throwable t) {
                t.addSuppressed(new IOException("Header mismatch for the SLAPI/0 savegame format! (likely wrong format)"));
                throw t;
            }
        }
        EventManager.handleEvent(new GalaxyLoadingEvent());
        DataInputStream dataIn = new DataInputStream(in);
        int version = dataIn.readInt();
        if (version != 0) {
            throw new IOException("Unknown version: " + version + ". Only version 0 is supported.");
        }
        dataIn.readInt(); // Discard amount of stars
        dataIn.readInt(); // Discard game year
        dataIn.readBoolean(); // Discard sandbox modifier
        dataIn.readLong(); // Discard save timestamp

        int keyCacheSize = dataIn.readInt();
        @NotNull NamespacedKey[] keyCache = new @NotNull NamespacedKey[keyCacheSize];
        for (int i = 0; i < keyCacheSize; i++) {
            keyCache[i] = NamespacedKey.fromString(dataIn.readUTF());
        }

        WriteableMetadataState metadataState = new WriteableMetadataState();
        for (int read = dataIn.readInt(); read != -1; read = dataIn.readInt()) {
            NamespacedKey metadataKey = keyCache[read];
            NamespacedKey encodingKey = keyCache[dataIn.readInt()];
            int len = LEB128.decodeUnsigned(dataIn);
            byte[] data = new byte[len];
            dataIn.readNBytes(data, 0, len);
            metadataState.add(metadataKey, encodingKey, data);
        }

        VanillaSavegameFormat.loadVanillaState(in);
        VanillaSavegameFormat.inferSavegameData();
        EventManager.handleEvent(new GalaxyLoadingEndEvent(this, metadataState));
    }

    @Override
    public synchronized void saveGameState(@NotNull OutputStream out, @Nullable String reason, @Nullable String location) throws IOException {
        if (reason == null) {
            reason = "Programmer issued save";
        }
        if (location == null) {
            location = "Unespecified";
        }
        // Obtain metadata from extensions
        MetadataCollector collector = new BasicMetadataCollector();
        EventManager.handleEvent(new GalaxySavingEvent(reason, location, collector));

        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.write(FORMAT_HEADER); // Format Header
        dataOut.writeInt(0); // Version
        dataOut.writeInt(Galimulator.getStarList().size()); // Amount of stars
        dataOut.writeInt(Galimulator.getGameYear()); // Game year
        dataOut.writeBoolean(Galimulator.hasUsedSandbox()); // Sandbox
        dataOut.writeLong(System.currentTimeMillis()); // Time

        Collection<@NotNull NamespacedKey> metadataKeys = collector.getKeys();
        Set<NamespacedKey> namespacedKeys = new HashSet<>(metadataKeys);
        Map<NamespacedKey, Integer> keyToId = new HashMap<>();
        Map<NamespacedKey, byte[]> serializedMetadata = new HashMap<>();
        Map<NamespacedKey, NamespacedKey> encoding = new HashMap<>();

        for (NamespacedKey key : metadataKeys) {
            Optional<Object> optional = collector.getDeserializedForm(key);
            if (!optional.isPresent()) {
                continue; // Discard object
            }
            @SuppressWarnings("null") // It is safe
            @NotNull
            Object obj = optional.get();
            Encoder<Object> encoder = Registry.CODECS.getEncoder(obj);
            if (encoder == null) {
                LoggerFactory.getLogger(getClass()).warn("Cannot serialize an object of instance "
                        + obj.getClass() + " which is the deserialized form of " + key + ". Did a mod forget to register a codec?");
                continue;
            }
            encoding.put(key, encoder.getEncodingKey());
            serializedMetadata.put(key, encoder.encode(obj));
            namespacedKeys.add(encoder.getEncodingKey());
        }

        dataOut.writeInt(namespacedKeys.size());
        int i = 0;
        for (NamespacedKey key : namespacedKeys) {
            dataOut.writeUTF(key.toString());
            keyToId.put(key, i++);
        }

        for (NamespacedKey key : metadataKeys) {
            byte[] serialized = serializedMetadata.get(key);
            if (serialized == null) {
                continue; // Previously discarded - discard again
            }
            dataOut.writeInt(keyToId.get(key));
            dataOut.writeInt(keyToId.get(encoding.get(key)));
            LEB128.encodeUnsigned(serialized.length, out);
            dataOut.write(serialized);
        }
        dataOut.writeInt(-1);

        try {
            VanillaSavegameFormat.saveVanillaState(out);
        } catch (Throwable var6) {
            if (var6 instanceof ThreadDeath) {
                throw (ThreadDeath) var6;
            }
            throw new IOException("Issue during serialisation.", var6);
        } finally {
            EventManager.handleEvent(new GalaxySavingEndEvent(location));
        }
    }

    @Override
    public boolean supportsSLAPIMetadata() {
        return true;
    }
}
