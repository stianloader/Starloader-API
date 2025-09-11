package de.geolykt.starloader.impl.util;

import java.util.Arrays;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.AvailableSince("2.0.0-a20250911")
public class LongRingBuffer {

    private final long data @NotNull[];
    private final int mask;
    private final int maxLength;
    private boolean overflown;
    private int pointerIndex;

    public LongRingBuffer(int maxLength) {
        if (Integer.bitCount(maxLength) != 1) {
            throw new IllegalStateException("Buffer must have a length that is a power of 2.");
        }
        this.data = new long[maxLength];
        this.maxLength = maxLength;
        this.mask = this.maxLength - 1;
    }

    public void appendValue(long value) {
        this.data[this.pointerIndex++] = value;
        if (this.pointerIndex == this.maxLength) {
            this.overflown = true;
            this.pointerIndex = 0;
        }
    }

    public void appendValue(long value, int frequency) {
        int endIndex = this.pointerIndex + frequency;
        if (endIndex >= this.maxLength) {
            while (frequency-- != 0) {
                this.appendValue(value);
            }
        } else {
            Arrays.fill(this.data, this.pointerIndex, endIndex, value);
            this.pointerIndex = endIndex;
        }
    }

    public long getHeadValue() {
        return this.data[(this.pointerIndex - 1) & this.mask];
    }

    public int getLength() {
        return this.overflown ? this.maxLength : this.pointerIndex;
    }

    public long getTailValue() {
        return this.overflown ? this.data[this.pointerIndex] : this.data[0];
    }
}
