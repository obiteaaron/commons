package com.github.obiteaaron.commons.util;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author Obite Aaron
 * @since 1.0
 */
public class MutableSemaphoreTest {

    @Test
    public void test() {
        final MutableSemaphore mutableSemaphore = new MutableSemaphore(10);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> mutableSemaphore.modifySemaphore(RandomUtils.nextInt(0, 10)), 0, 30, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 10; i++) {
            try {
                mutableSemaphore.acquire();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutableSemaphore.release();
            }
        }
        scheduledExecutorService.shutdown();
    }
}
