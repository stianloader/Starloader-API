package de.geolykt.starloader.impl.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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
        if (DEBUG) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter("SemaphoreLoopLockLog.csv", StandardCharsets.UTF_8.name());
            } catch (IOException e) {
                e.printStackTrace();
            }
            DEBUG_OUT = pw;
            writeDebug("transaction_id,transaction_type,count,start,end");
        } else {
            DEBUG_OUT = null;
        }
    }

    public SemaphoreLoopLock(int permits) {
        super(permits);
        this.scopes = new LockScope[permits];
        for (int i = 0; i < permits; i++) {
            this.scopes[i] = new SemaphoreLockScope(i);
        }
    }

    private static synchronized void writeDebug(String ln) {
        DEBUG_OUT.write(ln + "\r\n");
        DEBUG_OUT.flush();
    }

    @Override
    public void acquire() throws InterruptedException {
        if (DEBUG) {
            long debugId = DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquire();
            acquisitions.get().increment();
            writeDebug(debugId + ",ACQUIRE_ONE,1," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquire();
            acquisitions.get().increment();
        }
    }

    @Override
    public void acquire(int permits) throws InterruptedException {
        if (DEBUG) {
            long debugId = DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquire(permits);
            acquisitions.get().increment(permits);
            writeDebug(debugId + ",ACQUIRE," + permits + "," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquire(permits);
            acquisitions.get().increment(permits);
        }
    }

    @Override
    public void acquireHardControl() throws InterruptedException {
        if (getLocalAcquisitions() == 2) {
            return;
        } else if (getLocalAcquisitions() == 1) {
            acquire(1);
        } else {
            acquire(2);
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
        if (getLocalAcquisitions() > 0) {
            return;
        } else {
            acquire(1);
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
        if (DEBUG) {
            long debugId = DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquireUninterruptibly();
            acquisitions.get().increment();
            writeDebug(debugId + ",ACQUIRE_HARD_ONE,1," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquireUninterruptibly();
            acquisitions.get().increment();
        }
    }

    @Override
    public void acquireUninterruptibly(int permits) {
        if (DEBUG) {
            long debugId = DEBUG_ID_COUNTER.getAndIncrement();
            long start = System.currentTimeMillis();
            super.acquireUninterruptibly(permits);
            acquisitions.get().increment(permits);
            writeDebug(debugId + ",ACQUIRE_HARD," + permits + "," + start+ "," + System.currentTimeMillis());
        } else {
            super.acquireUninterruptibly(permits);
            acquisitions.get().increment(permits);
        }
    }

    @Override
    public int getAvailablePermits() {
        return super.availablePermits();
    }

    @Override
    public int getLocalAcquisitions() {
        return acquisitions.get().getValue();
    }

    @Override
    public void release() {
        if (acquisitions.get().getValue() < 1) {
            throw new IllegalMonitorStateException("This thread has no control over the semaphore");
        }
        super.release();
        acquisitions.get().decrement();
    }

    @Override
    public void release(int permits) {
        if (acquisitions.get().getValue() < permits) {
            throw new IllegalMonitorStateException("This thread has insufficent control over the semaphore");
        }
        super.release(permits);
        acquisitions.get().decrement(permits);
    }

    @Override
    public void releaseHard() {
        release(2);
    }

    @Override
    public void releaseSoft() {
        release(1);
    }

    @Override
    public boolean tryAcquire() {
        if (super.tryAcquire()) {
            acquisitions.get().increment();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquire(int permits) {
        if (super.tryAcquire(permits)) {
            acquisitions.get().increment(permits);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) throws InterruptedException {
        if (super.tryAcquire(permits, timeout, unit)) {
            acquisitions.get().increment(permits);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
        if (super.tryAcquire(timeout, unit)) {
            acquisitions.get().increment();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryAcquireHardControl() {
        if (getLocalAcquisitions() == 2) {
            return true;
        } else if (getLocalAcquisitions() == 1) {
            return tryAcquire(1);
        } else {
            return tryAcquire(2);
        }
    }

    @Override
    public boolean tryAcquireSoftControl() {
        if (getLocalAcquisitions() > 0) {
            return true;
        } else {
            return tryAcquire(1);
        }
    }
}
