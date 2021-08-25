package de.geolykt.starloader.api.gui.screen;

import org.jetbrains.annotations.NotNull;

/**
 * Metadata class for the {@link ScreenComponent} interface that dictates how line wrapping should behave
 * for the screen component.
 */
public class LineWrappingInfo {

    /**
     * Obtains an instance of {@link LineWrappingInfo} that always wraps, no matter the circumstances.
     *
     * @return See above
     */
    public static LineWrappingInfo alwaysWrapping() {
        return new LineWrappingInfo(true, true, true, true);
    }

    /**
     * Obtains an instance of {@link LineWrappingInfo} that only wraps if the object before it is of a different type, or
     * when the width of the screen is exhausted and no further objects can be added to the screen on that line.
     *
     * @return See above
     */
    public static LineWrappingInfo wrapDifferyType() {
        return new LineWrappingInfo(false, false, true, false);
    }

    public static @NotNull LineWrappingInfo wrapSameType() {
        return new LineWrappingInfo(false, false, false, true);
    }

    /**
     * Whether to unconditionally wrap before an object begins.
     */
    private final boolean wrapBeginOfObject;

    /**
     * Whether to insert a line wrap if the component before this component
     * is of another type, as queried by {@link ScreenComponent#isSameType(ScreenComponent)}.
     */
    private final boolean wrapDifferentType;

    /**
     * Whether to unconditionally wrap after the component ended.
     */
    private final boolean wrapEndOfObject;

    /**
     * Whether to insert a line wrap if the component before this component
     * is of the same type, as queried by {@link ScreenComponent#isSameType(ScreenComponent)}.
     */
    private final boolean wrapSameType;

    /*
     * Obtains the maximum length a line can get until it wraps.
     * May return -1 to indicate that it has no interest in the line width,
     * which means that it likely wraps at the parent screen's bounds.
     * This property only controls line wraps that are inserted just after the component,
     * not any line wraps before the component.
     *
     * @return The maximum width of the line, likely counted in pixels
     *
    public int getMaxLineWidth() {
        return maxLineWidth;
    }*/

    /**
     * The constructor.
     *
     * @param wrapBeginOfObject Whether to unconditionally wrap before an object begins.
     * @param wrapEndOfObject Whether to unconditionally wrap after the component ended.
     * @param wrapDifferentType The value returned by {@link #isWrapDifferentType()}
     * @param wrapSameType The value returned by {@link #isWrapSameType()}
     */
    public LineWrappingInfo(boolean wrapBeginOfObject, boolean wrapEndOfObject,
            boolean wrapDifferentType, boolean wrapSameType) {
        this.wrapBeginOfObject = wrapBeginOfObject;
        this.wrapEndOfObject = wrapEndOfObject;
        this.wrapDifferentType = wrapDifferentType;
        this.wrapSameType = wrapSameType;
    }

    /**
     * Whether to unconditionally wrap before an object begins.
     *
     * @return Whether to unconditionally wrap before an object begins.
     */
    public boolean isWrapBeginOfObject() {
        return wrapBeginOfObject;
    }

    /**
     * Whether to insert a line wrap if the component before this component
     * is of another type, as queried by {@link ScreenComponent#isSameType(ScreenComponent)}.
     *
     * @return See above
     */
    public boolean isWrapDifferentType() {
        return wrapDifferentType;
    }

    /**
     * Whether to unconditionally wrap after an object ends.
     *
     * @return Whether to unconditionally wrap after an object ends.
     */
    public boolean isWrapEndOfObject() {
        return wrapEndOfObject;
    }

    /**
     * Whether to insert a line wrap if the component before this component
     * is of the same type, as queried by {@link ScreenComponent#isSameType(ScreenComponent)}.
     *
     * @return See above
     */
    public boolean isWrapSameType() {
        return wrapSameType;
    }
}
