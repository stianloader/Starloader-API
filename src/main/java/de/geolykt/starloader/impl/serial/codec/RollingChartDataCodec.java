package de.geolykt.starloader.impl.serial.codec;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.graph.RollingChartData;
import de.geolykt.starloader.api.serial.Codec;

public final class RollingChartDataCodec<T> extends Codec<RollingChartData<T>> {
    @NotNull
    private static final RollingChartDataCodec<?> INSTANCE = new RollingChartDataCodec<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @NotNull
    public static final Codec<@NotNull RollingChartData> codec() {
        return (RollingChartDataCodec) INSTANCE;
    }

    private RollingChartDataCodec() {
        super(NamespacedKey.fromString("builtin", "rolling_chart"));
    }

    @Override
    public boolean canEncode(@NotNull Object object) {
        return object.getClass() == RollingChartData.class;
    }

    @Override
    @NotNull
    public RollingChartData<T> decode(byte @NotNull [] input) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(input);
                DataInputStream dataIn = new DataInputStream(bais)) {
            return this.decode(dataIn);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    @NotNull
    public RollingChartData<T> decode(@NotNull DataInputStream input) throws IOException {
        RollingChartData<T> data = new RollingChartData<>(4);
        data.serialDecode(input);
        return data;
    }

    @Override
    public byte @NotNull [] encode(@NotNull RollingChartData<T> input) {
        try {
            return input.serialEncode();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
