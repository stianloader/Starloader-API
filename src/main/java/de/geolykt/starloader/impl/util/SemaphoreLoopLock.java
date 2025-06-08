package de.geolykt.starloader.impl.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.utils.TickLoopLock;

public class SemaphoreLoopLock extends Semaphore implements TickLoopLock {

    private final class SemaphoreLockScope implements LockScope {

        private final int desiredPermits;

        public SemaphoreLockScope(int desiredPermits) {
            this.desiredPermits = desiredPermits;
        }

        @Override
        public void close() throws IllegalMonitorStateException {
            final int currentPermits = SemaphoreLoopLock.this.getLocalAcquisitions();
            if (currentPermits < this.desiredPermits) {
                throw new IllegalMonitorStateException("Before the scope was terminated, permits were erroneously released, "
                        + "which is why to return to the original state of " + this.desiredPermits + " acquired permits "
                        + (this.desiredPermits - currentPermits) + " permits would need to be acquired as currently only "
                        + currentPermits + " permits are held locally.");
            } else if (currentPermits > this.desiredPermits) {
                SemaphoreLoopLock.this.release(currentPermits - this.desiredPermits);
            }
        }

        @Override
        public int getDesiredAcquisitionsCount() {
            return this.desiredPermits;
        }
    }

    private static final long serialVersionUID = 3555178371578225965L;
    private final ThreadLocal<MutableInteger> acquisitions = ThreadLocal.withInitial(MutableInteger::new);
    private final LockScope scopes[];
    private static final boolean DEBUG = Boolean.getBoolean("de.geolykt.starloader.impl.util.SemaphoreLoopLock.DEBUG");
    private static final AtomicLong DEBUG_ID_COUNTER = new AtomicLong();
    private static final PrintWriter DEBUG_OUT;

    static {
        if (SemaphoreLoopLock.DEBUG) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter("SemaphoreLoopLockLog.csv", StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                e.printStackTrace();
            }
            DEBUG_OUT = pw;
            SemaphoreLoopLock.writeDebug("transaction_id,transaction_type,count,start,end");
        } else {
            DEBUG_OUT = null;
        }
    }

    public SemaphoreLoopLock(int permits) {
        super(permits);
        this.scopes = new LockScope[permits + 1];
        if (permits != 2) {
            try {
                throw new IllegalArgumentException("Expected permit count is 2, but this lock was initialized with " + permits + " permits! This may cause issues later on.");
            } catch (IllegalArgumentException e) {
                LoggerFactory.getLogger(SemaphoreLoopLock.class).error("Unexpected permit count when initializing a lock.", e);
            }
        }
        for (int i = 0; i <= permits; i++) {
            this.scopes[i] = new SemaphoreLockScope(i);
        }
    }

    private static synchronized void writeDebug(String ln) {
        SemaphoreLoopLock.DEBUG_OUT.write(ln + "\r\n");
        SemaphoreLoopLock.DEBUG_OUT.flush();
    }

    @Override
    public void acquire() throws InterruptedException {
        if (SemaphoreLoopLock.DEBUG) {
            long debugId = SemaphoreLoopLock.DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquire();
            this.acquisitions.get().increment();
            SemaphoreLoopLock.writeDebug(debugId + ",ACQUIRE_ONE,1," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquire();
            this.acquisitions.get().increment();
        }
    }

    @Override
    public void acquire(int permits) throws InterruptedException {
        if (SemaphoreLoopLock.DEBUG) {
            long debugId = SemaphoreLoopLock.DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquire(permits);
            this.acquisitions.get().increment(permits);
            SemaphoreLoopLock.writeDebug(debugId + ",ACQUIRE," + permits + "," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquire(permits);
            this.acquisitions.get().increment(permits);
        }
    }

    @Override
    public void acquireHardControl() throws InterruptedException {
        if (this.getLocalAcquisitions() == 2) {
            return;
        } else if (this.getLocalAcquisitions() == 1) {
            this.acquire(1);
        } else {
            this.acquire(2);
        }
    }

    @Override
    public LockScope acquireHardControlWithResources() throws InterruptedException {
        final int targetAcquisitions = this.getLocalAcquisitions();
        this.acquireHardControl();
        return this.scopes[targetAcquisitions];
    }

    @Override
    public void acquireSoftControl() throws InterruptedException {
        if (this.getLocalAcquisitions() > 0) {
            return;
        } else {
            this.acquire(1);
        }
    }

    @Override
    public LockScope acquireSoftControlWithResources() throws InterruptedException {
        final int targetAcquisitions = this.getLocalAcquisitions();
        this.acquireSoftControl();
        return this.scopes[targetAcquisitions];
    }

    @Override
    public void acquireUninterruptibly() {
        if (SemaphoreLoopLock.DEBUG) {
            long debugId = SemaphoreLoopLock.DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquireUninterruptibly();
            this.acquisitions.get().increment();
            SemaphoreLoopLock.writeDebug(debugId + ",ACQUIRE_HARD_ONE,1," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquireUninterruptibly();
            this.acquisitions.get().increment();
        }
    }

    @Override
    public void acquireUninterruptibly(int permits) {
        if (SemaphoreLoopLock.DEBUG) {
            long debugId = SemaphoreLoopLock.DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquireUninterruptibly(permits);
            this.acquisitions.get().increment(permits);
            SemaphoreLoopLock.writeDebug(debugId + ",ACQUIRE_HARD," + permits + "," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquireUninterruptibly(permits);
            this.acquisitions.get().increment(permits);
        }
    }

    /**
     * Force the release of a given amounts of permits, ignoring how many permits are held by the executing thread.
     *
     * <p>This method is inherently unsafe and needs to be used with utter caution. Appropriate usecases are when releasing
     * permits on behalf of another thread - for example in the case of a thread death.
     *
     * <p>This method behaves identically to {@link Semaphore#release(int)}.
     *
     * @param permits The amounts of permits to release.
     * @since 2.0.0
     * @see Semaphore#release(int)
     */
    @ApiStatus.Internal
    public void forceRelease(int permits) {
        super.release(permits);
    }

    @Override
    public int getAvailablePermits() {
        return super.availablePermits();
    }

    @Override
    public int getLocalAcquisitions() {
        return this.acquisitions.get().getValue();
    }

    @Override
    public void release() {
        if (this.acquisitions.get().getValue() < 1) {
            throw new IllegalMonitorStateException("This thread has no control over the semaphore");
        }
        super.release();
        this.acquisitions.get().decrement();
    }

    @Override
    public void release(int permits) {
        if (this.acquisitions.get().getValue() < permits) {
            throw new IllegalMonitorStateException("This thread has insufficent control over the semaphore");
        }
        super.release(permits);
        this.acquisitions.get().decrement(permits);
    }

    @Override
    public void releaseHard() {
        this.release(2);
    }

    @Override
    public void releaseSoft() {
        this.release(1);
    }

    @Override
    public boolean tryAcquire() {
        if (super.tryAcquire()) {
            this.acquisitions.get().increment();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquire(int permits) {
        if (super.tryAcquire(permits)) {
            this.acquisitions.get().increment(permits);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
        if (super.tryAcquire(permits, timeout, unit)) {
            this.acquisitions.get().increment(permits);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        if (super.tryAcquire(timeout, unit)) {
            this.acquisitions.get().increment();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquireHardControl() {
        if (this.getLocalAcquisitions() == 2) {
            return true;
        } else if (this.getLocalAcquisitions() == 1) {
            return this.tryAcquire(1);
        } else {
            return this.tryAcquire(2);
        }
    }

    @Override
    public boolean tryAcquireSoftControl() {
        if (this.getLocalAcquisitions() > 0) {
            return true;
        } else {
            return this.tryAcquire(1);
        }
    }
}
