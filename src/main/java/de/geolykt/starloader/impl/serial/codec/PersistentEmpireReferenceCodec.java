package de.geolykt.starloader.impl.serial.codec;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.serial.Codec;
import de.geolykt.starloader.api.serial.references.PersistentEmpireReference;
import de.geolykt.starloader.impl.JavaInterop;

public class PersistentEmpireReferenceCodec extends Codec<@NotNull PersistentEmpireReference> {

    @NotNull
    public static final Codec<@NotNull PersistentEmpireReference> INSTANCE = new PersistentEmpireReferenceCodec();

    private PersistentEmpireReferenceCodec() {
        super(NamespacedKey.fromString("builtin", "persistentempire"));
    }

    @Override
    public boolean canEncode(@NotNull Object object) {
        return object instanceof PersistentEmpireReference;
    }

    @Override
    public byte @NotNull [] encode(@NotNull PersistentEmpireReference input) {
        // Should a breaking change in the format occur -> create a new codec (with new namespaced key)

        byte[] empireName = input.getName().getBytes(StandardCharsets.UTF_8);
        byte[] backbuffer = new byte[16 + empireName.length];
        ByteBuffer bbuf = ByteBuffer.wrap(backbuffer);
        bbuf.putInt(input.getUID());
        bbuf.putInt(Color.rgba8888(input.getGdxColor()));
        bbuf.putInt(input.getFoundationYear());
        bbuf.putInt(input.getCollapseYear());
        bbuf.put(empireName);

        return backbuffer;
    }

    @Override
    @NotNull
    public PersistentEmpireReference decode(byte @NotNull [] input) {
        ByteBuffer bbuf = ByteBuffer.wrap(input);

        int uid = bbuf.getInt();
        Color color = new Color(bbuf.getInt());
        int foundationYear = bbuf.getInt();
        int collapseYear = bbuf.getInt();
        String name = new String(input, 16, input.length - 16, StandardCharsets.UTF_8);

        return new PersistentEmpireReference(uid, foundationYear, collapseYear, name, color);
    }

    @Override
    @NotNull
    public PersistentEmpireReference decode(@NotNull DataInputStream input) throws IOException {
        int uid = input.readInt();
        Color color = new Color(input.readInt());
        int foundationYear = input.readInt();
        int collapseYear = input.readInt();
        String name = new String(JavaInterop.readAllBytes(input), StandardCharsets.UTF_8);

        return new PersistentEmpireReference(uid, foundationYear, collapseYear, name, color);
    }

}
