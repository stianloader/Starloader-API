package de.geolykt.starloader.impl.usertest;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public abstract class Usertest {

    public static final List<Usertest> USERTESTS = new ArrayList<>();

    public abstract void runTest();

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract String getCategoryName();

    static {
        USERTESTS.add(new CanvasClickTest());
        USERTESTS.add(new MultiCanvasClickTest());
        USERTESTS.add(new MatryoshkaTest());
        USERTESTS.add(new CanvasScrollTest());
        USERTESTS.add(new CheckerboardTest());
        USERTESTS.add(new CanvasHoverTest());
        USERTESTS.add(new CanvasClippingTest());
        USERTESTS.add(new ShapesTest());
        USERTESTS.add(new StarlaneGenerationBenchmarks());
    }
}
