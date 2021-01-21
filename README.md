# Starloader-API

## Description

The starloader API is the more usefull part of the Starloader project for anyone
interested in the project. While Starloader proper is still required in order to
run, the Starloader-API will provide a common and hopefully version-independent
API that can be used by extensions. Another big goal is to prevent any conflicts
that may arise (for example overwriting static methods) via our Event API.

## Building

The project can be built via gradle. This can be easily done via `./gradlew build`
on most systems, however the [Starloader](https://github.com/Geolykt/Starloader) project must be
built and published to mavenLocal as well as the galimulator jar needs to be present
at the project root under `galimulator-desktop.jar`. To use the API within your 
project you need to publish the jar via 

    ./gradlew publicToMavenLocal


## Licensing and decompiled code

Most of the code is licensed under the Apache 2.0 license, however especially in
the mixin zone you may encounter decompiled and slightly altered code from 
Galimulator. I try to avoid these situations wherever possible, but sometimes
this cannot be avoided for mixin reasons (the mixin library does not allow to
inject into static methods). These snippets are marked by

    // Galimulator start -

followed by where exactly the code was pulled from. The snippets all end with a

    // Galimulator end

comment.
If any changes are performed that may change the signature of the method 
(i.e. not putting a `sourceclass.` or similar in front) then the changes will be
denotated by a 

    // Starloader -  

or 

    // Starloader start -

(if multiline) comment followed by the reason of the change and if the change
encompassed multiple lines, then it will end via a `// Starloader end` comment.
It should be addressed that the decompiled galimulator code IS NOT licensed
under the Apache 2.0 license and in fact this repo is doing legally grey area
stuff. Additionally the code is linking against code that doesn't have a known
license, which means that it is more legal grey area actions! 

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

A of the event API was borrowed from Bukkit, so if you have worked with Bukkit
before, the api will be pretty similar to you. The listener can then be
registered via

    EventManager.registerListener(new StarloaderDemoListener(logger));

however it should be noted that you cannot register the same listener multiple
times, although this should rearely be an issue for you.
