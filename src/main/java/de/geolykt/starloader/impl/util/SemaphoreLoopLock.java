package de.geolykt.starloader.impl.util;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import de.geolykt.starloader.api.utils.TickLoopLock;

public class SemaphoreLoopLock extends Semaphore implements TickLoopLock {

    private static final long serialVersionUID = 3555178371578225965L;
    private final ThreadLocal<MutableInteger> acquisitions = ThreadLocal.withInitial(MutableInteger::new);

    public SemaphoreLoopLock(int permits) {
        super(permits);
    }

    @Override
    public void acquire() throws InterruptedException {
        super.acquire();
        acquisitions.get().increment();
    }

    @Override
    public void acquire(int permits) throws InterruptedException {
        super.acquire(permits);
        acquisitions.get().increment(permits);
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
    public void acquireSoftControl() throws InterruptedException {
        if (getLocalAcquisitions() > 0) {
            return;
        } else {
            acquire(1);
        }
    }

    @Override
    public void acquireUninterruptibly() {
        super.acquireUninterruptibly();
        acquisitions.get().increment();
    }

    @Override
    public void acquireUninterruptibly(int permits) {
        super.acquireUninterruptibly(permits);
        acquisitions.get().increment(permits);
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
