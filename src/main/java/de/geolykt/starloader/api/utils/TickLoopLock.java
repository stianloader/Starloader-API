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
