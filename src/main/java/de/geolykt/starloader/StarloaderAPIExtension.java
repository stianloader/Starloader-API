package de.geolykt.starloader;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;

import net.minestom.server.extras.selfmodification.MinestomRootClassLoader;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.SignalExtensionTerminationEvent;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.SidebarInjector;
import de.geolykt.starloader.api.gui.modconf.ModConf;
import de.geolykt.starloader.api.gui.screen.ScreenBuilder;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryExpander;
import de.geolykt.starloader.api.resource.DataFolderProvider;
import de.geolykt.starloader.impl.DrawingManager;
import de.geolykt.starloader.impl.GalimulatorConfiguration;
import de.geolykt.starloader.impl.GalimulatorImplementation;
import de.geolykt.starloader.impl.SLSidebarInjector;
import de.geolykt.starloader.impl.asm.SLInstrinsicsTransformer;
import de.geolykt.starloader.impl.asm.SpaceASMTransformer;
import de.geolykt.starloader.impl.asm.StateActorCreatorTransformer;
import de.geolykt.starloader.impl.asm.UIASMTransformer;
import de.geolykt.starloader.impl.gui.SLComponentCreator;
import de.geolykt.starloader.impl.gui.SLScreenBuilder;
import de.geolykt.starloader.impl.registry.SLRegistryExpander;
import de.geolykt.starloader.impl.serial.codec.StringCodec;
import de.geolykt.starloader.impl.util.SLNoiseProvider;
import de.geolykt.starloader.mod.Extension;

/**
 * Entrypoint for the starloader API as an extension.
 * Absolutely not official API.
 */
@SuppressWarnings("resource")
public class StarloaderAPIExtension extends Extension {

    @Override
    public void preInitialize() {
        // We had to move this to preinit as some AWs are bork in SLL 2.0.0 and below, however
        // some of these versions are still supported by the current SLAPI version
        ModConf.setImplementation(new de.geolykt.starloader.impl.ModConf());
    }

    @Override
    public void unload() {
        EventManager.handleEvent(new SignalExtensionTerminationEvent(this));
        getLogger().info("SLAPI is going to bed. Let's conquer the stars tomorrow!");
    }

    /**
     * Register the "standard" built-in coders to the global codec registry.
     *
     * @since 2.0.0
     */
    private static void registerBuiltinCodecs() {
        Registry.CODECS.register(StringCodec.INSTANCE.getRegistryKey(), StringCodec.INSTANCE, String.class);
    }

    static {
        File dataFolder = new File("data");
        DataFolderProvider.setProvider(new DataFolderProvider.SimpleDataFolderProvider(dataFolder, new FileHandle(dataFolder), NullUtils.requireNotNull(dataFolder.toPath())));
        MinestomRootClassLoader.getInstance().addTransformer(new UIASMTransformer());
        MinestomRootClassLoader.getInstance().addTransformer(new SpaceASMTransformer());
        MinestomRootClassLoader.getInstance().addTransformer(new StateActorCreatorTransformer());
        MinestomRootClassLoader.getInstance().addTransformer(new SLInstrinsicsTransformer());
        Galimulator.setImplementation(new GalimulatorImplementation());
        Galimulator.setNoiseProvider(new SLNoiseProvider());
        Galimulator.setConfiguration(new GalimulatorConfiguration());
        Drawing.setImplementation(new DrawingManager());
        SidebarInjector.setImplementation(new SLSidebarInjector());
        ScreenBuilder.setFactory(SLScreenBuilder::new);
        ScreenBuilder.setComponentCreator(new SLComponentCreator());
        RegistryExpander.setImplementation(new SLRegistryExpander());
        registerBuiltinCodecs();
    }
}
