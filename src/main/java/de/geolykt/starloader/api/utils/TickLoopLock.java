package de.geolykt.starloader.api.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An abstraction layer over Galimulator's use of {@link Semaphore semaphores}.
 * This abstraction layer exists as it is possible that they will get replaced with
 * {@link ReentrantLock} in the future, which the developer of the game was not aware of
 * when first using {@link Semaphore}.
 *
 * <p>Furthermore, this interface provides the ability to check how many permits the
 * current thread has control of via the {@link #getAvailablePermits()} method. This improves
 * interoperability when it comes to communication between thread-safe and non-thread-safe code.
 * Example for when this is needed are savegames. A save or load method cannot otherwise know
 * whether the caller method has already acquired a lock or not.
 *
 * @since 2.0.0
 */
public interface TickLoopLock {

    /**
     * A {@link LockScope} is an object that represents a lock that is held for a given time.
     * They can be created via {@link TickLoopLock#acquireHardControlWithResources()} or
     * {@link TickLoopLock#acquireSoftControlWithResources()}. This object is meant to be used
     * in combination with try-with-resources as the {@link LockScope#close()} method will return
     * the {@link TickLoopLock} to a desired amount of acquired permits afterwards by releasing
     * the needed amount of permits. <b>It will not acquire permits to do.</b>
     *
     * @since 2.0.0
     * @implSpec Even though the API documentation considers this object to be a short-lived one,
     * the implementation may make instances of this interface long-lived for performance reasons.
     * This means that instances of this interface may be shared and represent multiple scopes internally,
     * but the API lacks the means to distinguish them.
     */
    public static interface LockScope extends AutoCloseable {

        /**
         * Closes the lock scope, releasing the needed amount of permits to return to the former state
         * as per {@link #getDesiredAcquisitionsCount()}. The amount of released permits can also be 0
         * if the lock is already in it's desired state.
         *
         * <p>This method will <b>never</b> acquire any permits to return to the desired state and instead
         * will throw a {@link IllegalMonitorStateException}.
         *
         * @throws IllegalMonitorStateException If returning to the original amount of locally acquired permits
         * was to require acquiring permits.
         * @since 2.0.0
         */
        @Override
        public void close() throws IllegalMonitorStateException;

        /**
         * Convenience method to return the amount of locally acquired permits before the {@link LockScope} was created.
         * This is also the amount of permits that are desired to be present immediately after the scope ends.
         *
         * @return The amount of permits acquired locally before the scope was created.
         * @since 2.0.0
         */
        public int getDesiredAcquisitionsCount();
    }

    /**
     * Acquires all permits. If any permit is not available, the method blocks until it is
     * {@link Thread#interrupt() interrupted} or the remaining permits were acquired.
     *
     * <p>Calling this method on the simulation loop lock means that the loading screen is shown.
     * The graphical and simulation loops are blocked as a result of such a call.
     *
     * <p>A soft lock <b>can</b> be directly upgraded to a hard lock by invoking this method.
     *
     * <p>This method returns instantly if the caller thread already has hard control.
     *
     * @see Semaphore#acquire(int)
     * @throws InterruptedException When the thread is interrupted while waiting to acquire the permit
     * @since 2.0.0
     */
    public void acquireHardControl() throws InterruptedException;

    /**
     * Acquire "hard" control over the tick loops as per {@link #acquireHardControl()} and return an {@link AutoCloseable}
     * which will release the hard control if the control was acquired - that is it will revert to the state before the lock
     * was acquired.
     *
     * <p>This method is mainly intended to be used in conjunction with try-with-resources (hence the name of the method)
     * which if used correctly can reduce the amount of erroneous releases of locks.
     *
     * <p>Note that due to technical reasons, the lock might get explicitly released while it is held even though the {@link AutoCloseable#close()}
     * was not invoked implicitly nor explicitly. However, once the close method is invoked, an {@link IllegalMonitorStateException} will be thrown
     * if the lock state does not meet the desired state.
     *
     * @return An {@link AutoCloseable} that once {@link AutoCloseable#close() closed}, will return the amount of {@link #getLocalAcquisitions()
     * locally acquired permits} to what it was at the state before this method was invoked.
     * @see #acquireHardControl()
     * @throws InterruptedException When the thread is interrupted while waiting to acquire the permit
     * @since 2.0.0
     */
    public LockScope acquireHardControlWithResources() throws InterruptedException;

    /**
     * Acquires a single permit. If the permit is not available, the method blocks until it is
     * {@link Thread#interrupt() interrupted} or a permit was acquired.
     *
     * <p>Calling this method on the simulation loop lock means that only the simulation loop is blocked.
     * The graphical tick loop will work as normal.
     *
     * <p>This method returns instantly if the caller thread already has either soft or hard control.
     *
     * @see Semaphore#acquire()
     * @throws InterruptedException When the thread is interrupted while waiting to acquire the permit
     * @since 2.0.0
     */
    public void acquireSoftControl() throws InterruptedException;

    /**
     * Acquire "soft" control over the tick loops as per {@link #acquireSoftControl()} and return an {@link AutoCloseable}
     * which will release the soft control if the control was acquired - that is it will revert to the state before the lock
     * was acquired.
     *
     * <p>This method is mainly intended to be used in conjunction with try-with-resources (hence the name of the method)
     * which if used correctly can reduce the amount of erroneous releases of locks.
     *
     * <p>Note that due to technical reasons, the lock might get explicitly released while it is held even though the {@link AutoCloseable#close()}
     * was not invoked implicitly nor explicitly. However, once the close method is invoked, an {@link IllegalMonitorStateException} will be thrown
     * if the lock state does not meet the desired state.
     *
     * @return An {@link AutoCloseable} that once {@link AutoCloseable#close() closed}, will return the amount of {@link #getLocalAcquisitions()
     * locally acquired permits} to what it was at the state before this method was invoked.
     * @see #acquireSoftControl()
     * @throws InterruptedException When the thread is interrupted while waiting to acquire the permit
     * @since 2.0.0
     */
    public LockScope acquireSoftControlWithResources() throws InterruptedException;

    /**
     * Obtains the amount of free permits available for the {@link Semaphore}.
     *
     * <p>This method can be indirectly used to estimate the current lock level
     * held by one or multiple thread. For the simulation loop lock this means the following:
     * <ul>
     *  <li>0 permits: Both Graphical and simulation loops running, or something blocks both.</li>
     *  <li>1 permit : Simulation loop running or something is blocking it.</li>
     *  <li>2 permits: Nothing blocking either loop and neither is running at the moment.</li>
     * </ul>
     *
     * @see Semaphore#availablePermits()
     * @return The amount of available permits
     * @since 2.0.0
     */
    public int getAvailablePermits();

    /**
     * Obtains the amount of acquisitions the current thread has performed.
     *
     * <p>This method can also be used to estimate the level of control of the current tick loop:
     * <ul>
     *  <li>0 permits: No control</li>
     *  <li>1 permit : "Soft" control</li>
     *  <li>2 permits: "Hard" control</li>
     * </ul>
     *
     * @return The amount of permits acquired by the calling thread
     * @since 2.0.0
     */
    public int getLocalAcquisitions();

    /**
     * Releases all acquired permits, throwing {@link IllegalMonitorStateException} if the executing
     * thread did not acquire a hard lock beforehand.
     *
     * @see #acquireHardControl()
     * @see #getLocalAcquisitions()
     * @see Semaphore#release(int)
     * @throws IllegalMonitorStateException Thrown if the executing thread did not acquire a hard lock.
     * @since 2.0.0
     */
    public void releaseHard();

    /**
     * Releases a single permit, throwing {@link IllegalMonitorStateException} if the executing
     * thread did not acquire a hard or a soft lock beforehand.
     *
     * <p>A hard lock <b>can</b> be directly downgraded to a soft lock by invoking this method.
     * Such behaviour can be beneficial if 
     *
     * @see #acquireSoftControl()
     * @see #getLocalAcquisitions()
     * @see Semaphore#release()
     * @throws IllegalMonitorStateException Thrown if the executing thread did not acquire a hard or a soft lock.
     * @since 2.0.0
     */
    public void releaseSoft();

    /**
     * Attempts to acquire all permits, returning instantly.
     *
     * <p>Calling this method on the simulation loop lock means that the loading screen is shown.
     * The graphical and simulation loops are blocked as a result of such a call.
     *
     * <p>This method returns {@code true} if the caller thread already has hard control.
     *
     * @see Semaphore#tryAcquire(int)
     * @since 2.0.0
     */
    public boolean tryAcquireHardControl();

    /**
     * Attempts to acquire a single permit, returning instantly.
     *
     * <p>Calling this method on the simulation loop lock means that only the simulation loop is blocked.
     * The graphical tick loop will work as normal.
     *
     * <p>This method returns {@code true} if the caller thread already has either soft or hard control.
     *
     * @see Semaphore#tryAcquire()
     * @since 2.0.0
     */
    public boolean tryAcquireSoftControl();
}
