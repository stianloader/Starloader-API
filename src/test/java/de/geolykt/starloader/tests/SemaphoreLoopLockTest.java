package de.geolykt.starloader.tests;

import org.junit.Test;

import de.geolykt.starloader.api.utils.TickLoopLock.LockScope;
import de.geolykt.starloader.impl.util.SemaphoreLoopLock;

public class SemaphoreLoopLockTest {

    @Test
    public void testDoubleAcquire() {
        SemaphoreLoopLock lock = new SemaphoreLoopLock(2);
        try (LockScope scope = lock.acquireHardControlWithResources()) {
            try (LockScope scope2 = lock.acquireHardControlWithResources()) {
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
