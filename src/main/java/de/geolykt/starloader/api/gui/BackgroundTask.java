package de.geolykt.starloader.api.gui;

import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.utils.TickLoopLock;

/**
 * Interface encapsulating background task indicators as displayed on the loading screen.
 * This includes the vanilla background task infrastructure, as well as
 * indicators for modded tasks (which may momentarily override the vanilla task).
 *
 * <p>Note that this interface and it's attached systems are purely visual, that is
 * they are only used for displaying the loading screen. The background tasks themselves
 * are meant to run asynchronous to the main rendering thread, and hence the calls to this
 * system. As such, all methods declared by this interface must be implemented in a way
 * where they are safe to call asynchronously. Further, it may be advisable to use
 * load and store fences where appropriate, which can be automatically induced by using
 * {@code volatile}. That being said, under most environments, failure to correctly
 * use {@code volatile} or manually setting fences has little consequences.
 *
 * <p>A background task is a task that is being performed if the main thread is unable to
 * acquire any ticking lock (as per {@link TickLoopLock#acquireSoftControl()}), usually
 * caused by another thread having acquired &quot;hard&quot; control of the tick loop lock
 * (as per {@link TickLoopLock#acquireHardControl()}. This scenario causes the
 * loading screen to show up.
 *
 * <p>This interface is safe to extend by API consumers,
 * although SLAPI may seek to implement this interface itself,
 * for example in order to wrap the vanilla background task logic.
 * As this class is meant to be extensible API, additional
 * methods to this interface must either be {@code static} or
 * {@code default}, in order to not raise
 * {@link AbstractMethodError AbstractMethodErrors} at runtime.
 * Another possible path to introduce newer APIs is interface
 * inheritance, meaning that a new class is created that extends
 * this class - similarly to how the {@link ConfigurationOption}
 * API behaves.
 *
 * <p>Keep in mind that a single {@link BackgroundTask} instance may be used to
 * describe multiple individual tasks. This is most critically the case with vanilla
 * tasks. Further, a single {@link BackgroundTask} might temporarily replace another
 * {@link BackgroundTask}, which will be reinstated after completion.
 *
 * @since 2.0.0-a20240831
 * @apiNote This interface is annotated with {@link NonBlocking}, meaning
 * that all methods of this interface are expected to be implemented in a
 * non-blocking manner. Ideally they are thread-safe, too.
 */
@AvailableSince("2.0.0-a20240831")
@NonBlocking
public interface BackgroundTask {

    /**
     * Obtains the progress description of this task.
     * More specifically, this is the string that defines what the task is
     * currently doing in a user-friendly manner. This will be the string that
     * is displayed in the loading menu.
     *
     * <p>This may also include the task progress as defined by
     * {@link #getTaskProgress()} or another user-friendly metric, provided
     * that such a progress can be obtained. Further, the format of the
     * string returned by this method can shift from implementation to implementation,
     * effectively reducing the scope in which this method can be used purely
     * to UIs.
     *
     * <p>Implementors should ensure a thread-safe implementation of this method.
     * In the worst case, returning a constant string would be such a thread-safe implementation.
     * Generally there isn't much one can do wrong here though unless one tries to do it wrong
     * knowingly, especially after using {@link AtomicInteger AtomicIntegers} or similar atomic types.
     *
     * @return A description of the work and progress of said work of this
     * {@link BackgroundTask}.
     * @since 2.0.0-a20240831
     */
    @NotNull
    public String getProgressDescription();

    /**
     * Obtains the progress of this task, returning a value between {@code 0} and {@code 1}
     * (both inclusive). If the task has an unascertainable progress (e.g. due
     * to opaque design of the underlying code), a value of {@code 0} shall be returned.
     *
     * <p>The &quot;progress&quot; of a task can mean a variety of things: That is, not
     * only can it be used as an estimation of how much time is remaining versus
     * the time that has passed, it can also be an indicator of how many stages
     * have been completed, or the amounts of bytes transferred versus the amounts
     * of bytes expected. In many cases, small hiccups are expected - as in the
     * progress will not increment evenly. Combined with the fact that the scope
     * of a task is not well defined due to a lack of a task hierarchy, the value
     * for returned by this method is unappealing for a progress bar in it's
     * current state. That being said, this method was primarily included in the final
     * draft of the {@link BackgroundTask} interface as the loading menu may be
     * overhauled in a later point in time (if not by vanilla galimulator, then by a mod),
     * where a progress bar can be appealing, especially if combined with additional
     * APIs implementing a task hierarchy (however such a hierarchy isn't implemented
     * at the point of drafting [2024-08-31] in order to avoid APIs disconnected from
     * reality).
     *
     * <p>Using this method to check whether a task has deadlock can be another
     * use of this method, however please beware that some long-running tasks might
     * not implement this method in a way where it will smoothly increment.
     * Further, in some other deadlocks, the value might continually increment even past
     * 1 or continually reset progress. While incrementing past 1 (or decrementing below 0
     * for that matter) would be an incorrect implementation of this method, one should
     * be aware that a deadlock is inherently an exceptional state caused by insufficient
     * testing. Henceforth, any watchdog systems using this method should be very careful
     * and should try to use multiple systems to evaluate deadlocks where possible.
     *
     * <p>It is valid for an implementation of this method to &quot;destroy&quot; progress,
     * that is repeated invocations of this method may result in decreasing results.
     * This is especially the case if the progress was estimated based on assumptions
     * that were found to be incorrect or invalid.
     *
     * <p>SLAPI does not make use of this method itself. For all vanilla tasks
     * which are wrapped by the {@link BackgroundTask} API, the progress
     * of these tasks cannot be estimated (due to a lack of functioning APIs
     * for that in vanilla Galimulator), which is why for vanilla tasks, this
     * method always returns the value of {@code 0}.
     *
     * @return The current progress of this task, a float value between 0 and 1.
     * @since 2.0.0-a20240831
     */
    public float getTaskProgress();
}
