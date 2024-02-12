package de.geolykt.starloader.impl.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.impl.registry.SLRegistryExpander.SLStarlaneGeneratorPrototype;

import snoddasmannen.galimulator.Space.ConnectionMethod;

class SLStarlaneGenerator extends ConnectionMethod {

    private static final long serialVersionUID = 4794391682639685435L;

    @NotNull
    private final Runnable callback;

    SLStarlaneGenerator(int ordinal, @NotNull SLStarlaneGeneratorPrototype prototype) {
        super(prototype.enumName, ordinal, prototype.name);
        this.callback = prototype.callback;
    }

    @Override
    public void a() {
        this.callback.run();
    }
}
