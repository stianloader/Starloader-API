package de.geolykt.starloader.impl.usertest;

import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.StarlaneGenerator;
import de.geolykt.starloader.api.gui.BasicDialogBuilder;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.KeystrokeInputHandler;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeys;
import de.geolykt.starloader.impl.GalimulatorImplementation;
import de.geolykt.starloader.impl.gui.keybinds.KeybindHelper;
import de.geolykt.starloader.impl.registry.Registries;
import de.geolykt.starloader.impl.util.SemaphoreLoopLock;

import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.ProceduralStarGenerator;
import snoddasmannen.galimulator.Space;

public class StarlaneGenerationBenchmarks extends Usertest {

    private static class GeneratorBenchmarkRow {
        private Label[] labelTimeTotal;
        private Label[] labelTimeAverage;
        private Label[] labelLanesTotal;
        private Label[] labelLanesAverage;
        private Label[] labelLanesPerS;
        private Label[] labelRunCount;
        private BigDecimal[] totalTime;
        private BigDecimal[] laneTotal;
        private BigDecimal[] runCount;
        private int starCount;
        private String generator;

        public GeneratorBenchmarkRow(String generator, int count) {
            this.generator = generator;
            this.starCount = count;
        }
    }

    private static class DisposableBackgroundStage extends Stage {

        @NotNull
        private final Drawable background;
        private final boolean disposeBackground;

        public DisposableBackgroundStage(@NotNull Drawable background, boolean disposeBackground) {
            super(new ScreenViewport());
            this.background = background;
            this.disposeBackground = disposeBackground;
        }

        @Override
        public void draw() {
            this.getBatch().setProjectionMatrix(this.getViewport().getCamera().projection);
            this.getBatch().setColor(1, 1, 1, 1);
            this.getBatch().begin();
            float w = this.getViewport().getWorldWidth();
            float h = this.getViewport().getWorldHeight();
            this.background.draw(this.getBatch(), -w/2, -h/2, w, h);
            this.getBatch().end();
            super.draw();
        }

        @Override
        public void dispose() {
            super.dispose();
            if (!this.disposeBackground) {
                return;
            }
            if (this.background instanceof Disposable) {
                ((Disposable) this.background).dispose();
            } else if (this.background instanceof TextureRegionDrawable) {
                ((TextureRegionDrawable) this.background).getRegion().getTexture().dispose();
            }
        }
    }

    @Override
    public void runTest() {
        new BasicDialogBuilder("Confirm benchmark", "Warning: Benchmarking starlane generators involves discarding your current galaxy. Are you sure you wish to proceed anyways?")
                .playCloseSound()
                .setChoices(Arrays.asList("[RED]YES[]", "[GREEN]NO[]"))
                .setDuration(10)
                .addCloseListener((cause, text) -> {
                    if ("[RED]YES[]".equals(text)) {
                        Galimulator.runTaskOnNextFrame(() -> {
                            this.runBenchmark();
                        });
                    }
                })
                .show();
    }

    public void runBenchmark() {
        Galimulator.setPaused(true);
        Stage displayStage = new DisposableBackgroundStage(new BaseDrawable(), false);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.align(Align.top);
        List<GeneratorBenchmarkRow> rows = new ArrayList<>();
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL",    500));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL",    750));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL",  1_000));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL",  2_000));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL",  5_000));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL",  7_500));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL", 10_000));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL", 25_000));
        rows.add(new GeneratorBenchmarkRow("STRETCHED_SPIRAL", 50_000));
        GeneratorBenchmarkRow[] benchmarks = rows.toArray(new GeneratorBenchmarkRow[0]);
        StarlaneGenerator[] generators;

        {
            LabelStyle labelStyle = new LabelStyle(Drawing.getSpaceFont(), Color.WHITE);
            {
                Label headerLabel = new Label("Starlane generator benchmarks", labelStyle);
                headerLabel.setFontScale(2F);
                rootTable.add(headerLabel).fillX();
            }
            rootTable.row();
            {
                Label subheaderLabel = new Label("Press ESCAPE to escape, CTRL + C to copy data to clipboard and F11 to toggle fullscreen", labelStyle);
                rootTable.add(subheaderLabel).fillX();
            }
            rootTable.row();
            Table contentTable = new Table();
            ScrollPane scrollPane = new ScrollPane(contentTable);
            contentTable.debugCell();
            contentTable.padLeft(5F);
            contentTable.padRight(5F);
            rootTable.add(scrollPane).fill();
            if (Registry.STARLANE_GENERATORS == null ) {
                Registries.initConnectionMethods();
            }
            generators = Registry.STARLANE_GENERATORS.getValues();
            contentTable.add();
            contentTable.add();
            for (StarlaneGenerator generator : generators) {
                contentTable.add(new Label(generator.getDisplayName(), labelStyle));
            }
            contentTable.row();
            for (GeneratorBenchmarkRow benchmark : benchmarks) {
                benchmark.totalTime = new BigDecimal[generators.length];
                benchmark.laneTotal = new BigDecimal[generators.length];
                benchmark.runCount = new BigDecimal[generators.length];
                benchmark.labelTimeTotal = new Label[generators.length];
                benchmark.labelTimeAverage = new Label[generators.length];
                benchmark.labelLanesTotal = new Label[generators.length];
                benchmark.labelLanesAverage = new Label[generators.length];
                benchmark.labelLanesPerS = new Label[generators.length];
                benchmark.labelRunCount = new Label[generators.length];
                for (int i = 0; i < generators.length; i++) {
                    benchmark.totalTime[i] = new BigDecimal(0);
                    benchmark.laneTotal[i] = new BigDecimal(0);
                    benchmark.runCount[i] = new BigDecimal(0);
                }
                contentTable.row();
                contentTable.add(new Label(benchmark.generator + "\n" + benchmark.starCount, labelStyle)).align(Align.left);
                contentTable.add(new Label("runs", labelStyle)).align(Align.left);
                for (int i = 0; i < generators.length; i++) {
                    contentTable.add((benchmark.labelRunCount[i] = new Label("---", labelStyle))).align(Align.left);
                }
                contentTable.row();
                contentTable.add();
                contentTable.add(new Label("time[ms]", labelStyle)).align(Align.left);
                for (int i = 0; i < generators.length; i++) {
                    contentTable.add((benchmark.labelTimeTotal[i] = new Label("---", labelStyle))).align(Align.left);
                }
                contentTable.row();
                contentTable.add();
                contentTable.add(new Label("lanes", labelStyle)).align(Align.left);
                for (int i = 0; i < generators.length; i++) {
                    contentTable.add((benchmark.labelLanesTotal[i] = new Label("---", labelStyle))).align(Align.left);
                }
                contentTable.row();
                contentTable.add();
                contentTable.add(new Label("lanes/run", labelStyle)).align(Align.left);
                for (int i = 0; i < generators.length; i++) {
                    contentTable.add((benchmark.labelLanesAverage[i] = new Label("---", labelStyle))).align(Align.left);
                }
                contentTable.row();
                contentTable.add();
                contentTable.add(new Label("ms/run", labelStyle)).align(Align.left);
                for (int i = 0; i < generators.length; i++) {
                    contentTable.add((benchmark.labelTimeAverage[i] = new Label("---", labelStyle))).align(Align.left);
                }
                contentTable.row();
                contentTable.add();
                contentTable.add(new Label("lanes/s", labelStyle)).align(Align.left);
                for (int i = 0; i < generators.length; i++) {
                    contentTable.add((benchmark.labelLanesPerS[i] = new Label("---", labelStyle))).align(Align.left);
                }
            }
        }
        displayStage.addActor(rootTable);
        AtomicBoolean execute = new AtomicBoolean(true);

        Thread benchmarkingThread = new Thread(() -> {
            LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).info("Obtaining tick loop lock");
            try {
                Galimulator.getSimulationLoopLock().acquireHardControl();
            } catch (InterruptedException e) {
                LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).error("Unable to obtain tick loop lock", e);
                return;
            }
            LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).info("Tick loop lock obtained");
            while (true) {
                for (GeneratorBenchmarkRow benchmark : benchmarks) {
                    Space.generateGalaxy(benchmark.starCount, new MapData(ProceduralStarGenerator.valueOf(benchmark.generator)));
                    for (int i = 0; i < generators.length; i++) {
                        StarlaneGenerator generator = generators[i];
                        if ((benchmark.starCount > 1000 && generator.getRegistryKey().equals(RegistryKeys.GALIMULATOR_STARLANES_TOTAL_CONNECTION))
                                || (benchmark.starCount > 5000 && generator.getRegistryKey().equals(RegistryKeys.GALIMULATOR_STARLANES_STARS_ON_A_STRING))) {
                            // These algorithms are simply too inefficient to be used at this scale
                            benchmark.labelTimeTotal[i].setText("SKIPPED");
                            benchmark.labelRunCount[i].setText("SKIPPED");
                            benchmark.labelLanesTotal[i].setText("SKIPPED");
                            benchmark.labelTimeAverage[i].setText("SKIPPED");
                            benchmark.labelLanesAverage[i].setText("SKIPPED");
                            benchmark.labelLanesPerS[i].setText("SKIPPED");
                            continue;
                        }
                        if (!execute.get()) {
                            LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).info("Releasing tick loop lock");
                            Galimulator.getSimulationLoopLock().releaseHard();
//                            execute.set(true);
//                            execute.notify();
                            return;
                        }
                        for (Star s : Galimulator.getStarList()) {
                            for (Star neighbour : s.getNeighbourList().toArray(new @NotNull Star[0])) {
                                s.removeNeighbour(neighbour);
                                neighbour.removeNeighbour(s);
                            }
                        }
                        long start = System.currentTimeMillis();
                        generator.generateStarlanes();
                        long time = System.currentTimeMillis() - start;
                        int laneCount = 0;
                        for (Star s : Galimulator.getStarList()) {
                            for (Star neighbour : s.getNeighbourList()) {
                                if (s.getUID() < neighbour.getUID()) {
                                    laneCount++;
                                }
                            }
                        }
                        benchmark.runCount[i] = benchmark.runCount[i].add(BigDecimal.ONE);
                        benchmark.totalTime[i] = benchmark.totalTime[i].add(BigDecimal.valueOf(time));
                        benchmark.laneTotal[i] = benchmark.laneTotal[i].add(BigDecimal.valueOf(laneCount));
                        benchmark.labelTimeTotal[i].setText(benchmark.totalTime[i].toEngineeringString());
                        benchmark.labelRunCount[i].setText(benchmark.runCount[i].toEngineeringString());
                        benchmark.labelLanesTotal[i].setText(benchmark.laneTotal[i].toEngineeringString());
                        benchmark.labelTimeAverage[i].setText(benchmark.totalTime[i].divide(benchmark.runCount[i], BigDecimal.ROUND_HALF_EVEN).toEngineeringString());
                        benchmark.labelLanesAverage[i].setText(benchmark.laneTotal[i].divide(benchmark.runCount[i], BigDecimal.ROUND_HALF_EVEN).toEngineeringString());
                        benchmark.labelLanesPerS[i].setText(benchmark.laneTotal[i].multiply(BigDecimal.valueOf(1000)).divide(benchmark.totalTime[i], BigDecimal.ROUND_HALF_EVEN).toEngineeringString());
                        LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).info("Ran {} at benchmark {} with {} stars within {}ms and {} total lanes", generator.getDisplayName(), benchmark.generator, benchmark.starCount, time, laneCount);
                    }
                }
            }
        }, "Starlane benchmarker thread");

        benchmarkingThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                assert e != null;
                Gdx.app.postRunnable(() -> {
                    Drawing.setShownStage(null);
                    ((SemaphoreLoopLock) Galimulator.getSimulationLoopLock()).forceRelease(2);
                    GalimulatorImplementation.crash(e, "Uncaught exception while benchmarking. This is likely mod caused, report this to the respective mod developers.", false);
                });
            }
        });

        displayStage.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (!(event instanceof InputEvent)) {
                    return false;
                }
                InputEvent evt = (InputEvent) event;
                if (evt.getType() == Type.keyDown && evt.getKeyCode() == Keys.C && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))) {
                    // TODO Implement CTRL + C
                } else if (evt.getType() == Type.keyDown && evt.getKeyCode() == Keys.ESCAPE) {
                    execute.set(false);
                    LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).info("Recieved ESCAPE interrupt");
//                    while (!execute.get()) {
//                        try {
//                            execute.wait(10_000L);
//                        } catch (InterruptedException e) {
//                            LoggerFactory.getLogger(StarlaneGenerationBenchmarks.class).warn("Wait for benchmark thread interrupted!", e);
//                            break;
//                        }
//                    }
                    Drawing.setShownStage(null);
                } else if (evt.getType() == Type.keyDown && evt.getKeyCode() == Keys.F11) {
                    KeystrokeInputHandler.getInstance().getKeybinds().forEach((keybind) -> {
                        if (keybind.getID().equals(KeybindHelper.FULLSCREEN)) {
                            keybind.executeAction();
                        }
                    });
                }
                return true;
            }
        });
        Drawing.setShownStage(displayStage);

        benchmarkingThread.start();

        //displayStage.setDebugAll(true);
    }

    @Override
    @NotNull
    public String getName() {
        return "Benchmark starlane generators";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }

}
