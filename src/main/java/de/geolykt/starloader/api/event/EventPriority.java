package de.geolykt.starloader.api.event;

public enum EventPriority {

    /**
     * The least important handler priority, called first and can be used to modify
     * the event granularly.
     */
    HIGHEST,

    /**
     * The less important handler priority, called at second place and can be used
     * to modify the event granularly.
     */
    HIGH,

    /**
     * The default handler priority, initial modification to the event should be
     * done by this stage.
     */
    MEDIUM,

    /**
     * The more important priority, it will be called a bit later.
     */
    LOW,

    /**
     * The most important priority, it will be called late and should be used for
     * final cancellations or similar.
     */
    LOWEST,

    /**
     * A pure monitoring priority, it will be called last and cancelling the event
     * will result in a nag.
     */
    MONITOR
}
