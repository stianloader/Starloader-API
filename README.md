# Starloader-API

## Communication

We do have an IRC channel for anyone that is interested in modding the game in
general, although the main purpose of the channel will be to discuss Starloader.
Feel free to take a look at it at #galimulator-modding @ irc.esper.net
I should note that I'm not active 24/7 on there, so have patience.
If you live in europe, then you'll have the best chances of meeting me.

There is also a discord if you are into this: https://discord.gg/CjnPMxsAX6

## Description

The starloader API is the more usefull part of the Starloader project for anyone
interested in the project. While Starloader proper is still required in order to
run, the Starloader-API will provide a common and hopefully version-independent
API that can be used by extensions. Another big goal is to prevent any conflicts
that may arise (for example overwriting static methods) via our Event API.

## Building

Warning: **Building SLAPI requires Java 11 or above**

The project can be built via gradle as you are used to. Simply running
`./gradlew build` on UNIX (on Windows you'd want to use `gradlew.bat`) will
build the project and everything else that you need. The output will be in the
`build/libs` folder.

### IDE Support

Simply import this repo as a gradle project and you are set to go!
If you want to debug this mod within your eclipse IDE you can run the
`genEclipseRuns` task - which generates the `runMods.launch` file that
you can run in order to debug the starloader api.

### Maven

You can use the maven package `de.geolykt:starloader-api:CURRENT_VERSION_HERE`
at `https://geolykt.de/maven`

To bind against the latest version of SLAPI run

    java -jar brachyura-bootstrap-0.jar publishToMavenLocal

which publishes the project to your local maven repository. You can then use it
without having to declare a repository explicitly. Just note that doing
this requires any contributors of your project to do the same.

## Which version for what?

- 3cffffb5bd0f208712deadd98e0efeb2fa38251e (N/A)  -> Galimulator 4.7
- 08ef68c9883a076279bee8aa9e7e09937312852c (v1.2) -> Galimulator 4.8 + 4.8-beta.2
- a2a1d9a597bf2463247bc0468e4b8594c9d96ef2 (v1.5) -> Galimulator 4.9
- 7a341bfc108e863629cd64763c5de92e6baf894b (v2.0) -> Galimulator 4.10

## Existing Extensions making use of this API (that I know of)

- [Datadriven-specials](https://github.com/Geolykt/Datadriven-specials)
- [Timelapser](https://github.com/Geolykt/Timelapser)
- [Variable Data Folder](https://github.com/Geolykt/VariableDataFolder)
- [FeedbackVectors](https://github.com/Geolykt/Feedbackvectors)
- [GalimulatorIRC](https://github.com/Geolykt/GalimulatorIRC)

## Licensing and decompiled code

Most of the code is licensed under the Apache 2.0 license, however especially in
the mixin zone you may encounter decompiled and slightly altered code from
Galimulator. It should be addressed that the decompiled galimulator code IS NOT
licensed under the Apache 2.0 license and in fact this repo is doing legally grey
area stuff. Additionally the code is linking against code that doesn't have a
known license, which means that it is more legal grey area actions! Although for
latter I did get a not really legally valid permisson, so it's not much to
worry about.

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
            logger.info("{} ceased to exist. They persisted for {} years.",
                    event.getCollapsedEmpire().getEmpireName(),
                    event.getCollapsedEmpire().getAge());
        }
    }

    @EventHandler
    public void onAllianceJoin(AllianceJoinEvent event) {
        logger.info("{} has joined the alliance \"{}\".", 
                event.getEmpire().getEmpireName(), 
                event.getAlliance().getFullName());
    }

    @EventHandler
    public void onAllianceQuit(AllianceLeaveEvent event) {
        logger.info("{} has left the alliance \"{}\".", 
                event.getEmpire().getEmpireName(), 
                event.getAlliance().getAbbreviation());
    }
}
```

The concept behind the event API was borrowed from Bukkit, so if you have worked
with Bukkit before, the api will be pretty similar to you.
The listener can then be registered via

    EventManager.registerListener(new StarloaderDemoListener(logger));

however it should be noted that you cannot register the same listener instance multiple
times, although this should rarely be an issue for you. It should also be noted
that the event API is very fragile at the moment and you should avoid
registering or unregistering listeners an incredible amount of times as that
can be very resource intensive.

