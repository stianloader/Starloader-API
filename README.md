# SLAPI

## Communication

We do have an IRC channel for anyone that is interested in modding the game in
general, although the main purpose of the channel will be to discuss Starloader.
Feel free to take a look at it at #galimulator-modding @ irc.esper.net
I should note that I'm not active 24/7 on there, so have patience.
If you live in europe, then you'll have the best chances of meeting me.

There is also a discord if you are into this: https://discord.gg/CjnPMxsAX6

## Description

SLAPI is the more usefull part of the stianloader project for anyone
interested in the project. While SLL is still required in order to
run all mods in the Galimulator modding ecosystem, the Starloader-API provides
a common and ideally version-independent API that can be used by extensions.
Another big goal is to prevent any conflicts that may arise (for example
overwriting methods via Mixins or doing other potentially conflicting transforms)
via our Event API.

## Building

Warning: **Building SLAPI requires Java 11 or above**

The project can be built via gradle as you are used to. Simply running
`./gradlew build` on UNIX (on Windows you'd want to use `gradlew.bat`) will
build the project and everything else that you need. The output will be in the
`build/libs` folder. The `-remapped.jar` artifact is the jar used in a
production environment, the other jar will only work in development
environments with the appropriate mappings applied (that is the minimal
mappings. Supplying additional mappings to gslStarplane means that this
artifact cannot be used in that development environment. See the documentation
of gslStarplane for workarounds in this scenario.)

### IDE Support

Simply import this repo as a gradle project and you are set to go!
If you want to debug this mod within your eclipse IDE you can run the
`genEclipseRuns` task - which generates the `runMods.launch` file that
you can run in order to debug the starloader api.

### Maven

You can use the maven package `de.geolykt:starloader-api:CURRENT_VERSION_HERE`
at `https://stianloader.org/maven/`. Releases on the stianloader.org repository
are performed at a nightly basis, that is every commit should see a new
alpha release. "Full" releases may occur from time to time - however
a few years have passed since the last full release, so one shouldn't depend
on the presence of such releases.

Warning: **Publishing SLAPI requires Java 17 or above** (Javadocs needs it)

To bind against your own version of SLAPI run

    ./gradlew publish

which publishes the project to your local maven repository. You can then use it
without having to declare a repository explicitly. Just note that doing
this requires any contributors of your project to do the same.

## Which version for what?

- 3cffffb5bd0f208712deadd98e0efeb2fa38251e (N/A)  -> Galimulator 4.7
- 08ef68c9883a076279bee8aa9e7e09937312852c (v1.2) -> Galimulator 4.8 + 4.8-beta.2
- a2a1d9a597bf2463247bc0468e4b8594c9d96ef2 (v1.5) -> Galimulator 4.9
- 7a341bfc108e863629cd64763c5de92e6baf894b (v2.0) -> Galimulator 4.10

## Existing mods making use of this API (that I know of)

- [FastStar](https://github.com/Geolykt/FastStar)
- [galimulator-profiler](https://github.com/Geolykt/galimulator-profiler)
- [StarCellShading](https://github.com/Geolykt/StarCellShading)
- [fast-asynchronous-starlane-triangulator](https://github.com/Geolykt/fast-async-starlane-triangulator)
- [GalimulatorIRC](https://github.com/Geolykt/GalimulatorIRC)
- [s2dmenues](https://github.com/Geolykt/s2dmenues)

### Legacy mods (i.e. mods that haven't been developed and tested for a while)

- [Datadriven-specials](https://github.com/Geolykt/Datadriven-specials)
- [Timelapser](https://github.com/Geolykt/Timelapser)
- [IvyH](https://github.com/Geolykt/IvyH)

## Licensing and legal concerns

The code of this repository is licensed under the Apache 2.0 license.
However, due to the inherent state of modding, the existence of this
project mostly relies on the (silent) approval of the game's developer.
Henceforth, we ask anyone using this project to use it in a way which
would not harm the perception of this project. That being said, from
a legal perspective our appeal isn't binding at all.

## Event API example

```java
package de.geolykt.starloader.demo;

import org.slf4j.Logger;

import de.geolykt.starloader.api.event.EventHandler;
import de.geolykt.starloader.api.event.EventPriority;
import de.geolykt.starloader.api.event.Listener;
import de.geolykt.starloader.api.event.alliance.AllianceJoinEvent;
import de.geolykt.starloader.api.event.alliance.AllianceLeaveEvent;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent;

public class StarloaderDemoListener implements Listener {

    private final Logger logger;

    public StarloaderDemoListener(Logger log) {
        logger = log;
    }

    @EventHandler(EventPriority.MEDIUM)
    public void onEmpireCollapse(EmpireCollapseEvent event) {
        if (!event.isCancelled()) {
            this.logger.info("{} ceased to exist. They persisted for {} years.",
                    event.getEmpire().getEmpireName(),
                    event.getEmpire().getAge());
        }
    }

    @EventHandler
    public void onAllianceJoin(AllianceJoinEvent event) {
        this.logger.info("{} has joined the alliance \"{}\".", 
                event.getJoiningEmpire().getEmpireName(), 
                event.getAlliance().getFullName());
    }

    @EventHandler
    public void onAllianceQuit(AllianceLeaveEvent event) {
        this.logger.info("{} has left the alliance \"{}\".", 
                event.getLeavingEmpire().getEmpireName(), 
                event.getAlliance().getAbbreviation());
    }
}
```

The concept behind the event API was borrowed from Bukkit, so if you have worked
with Bukkit before, the api will look pretty similar to you.
The listener can then be registered via

    EventManager.registerListener(new StarloaderDemoListener(logger));

however it should be noted that you cannot register the same listener instance multiple
times, although this should rarely be an issue for you. It should also be noted
that the event API is very fragile at the moment and you should avoid
registering or unregistering listeners frequently as that can be very resource
intensive.

