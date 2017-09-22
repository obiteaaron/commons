package com.github.obiteaaron.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可变信号量，没有提供大于1的信号量，因为这可能会导致永久等待
 *
 * @author Obite Aaron
 * @since 1.0
 */
public class MutableSemaphore {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger currentSemaphore = new AtomicInteger(4);

    private final Semaphore semaphore = new Semaphore(currentSemaphore.get());

    public MutableSemaphore() {
    }

    public MutableSemaphore(int initSemaphoreNumber) {
        modifySemaphore(initSemaphoreNumber);
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

    public synchronized void modifySemaphore(int newNumber) {
        if (newNumber <= 0) {
            logger.error("Semaphores cannot less than 1.");
            return;
        }
        int oldNumber = currentSemaphore.get();
        if (oldNumber == newNumber) {
            logger.info("Semaphores do not need to change.");
            return;
        }
        if (oldNumber > newNumber) {
            int diff = oldNumber - newNumber;
            try {
                semaphore.acquire(diff);
                currentSemaphore.set(newNumber);
                logger.info("The Semaphores is modified to " + newNumber);
            } catch (InterruptedException e) {
                logger.error("The Semaphores is modified error", e);
            }
            return;
        }
        if (oldNumber < newNumber) {
            int diff = newNumber - oldNumber;
            semaphore.release(diff);
            currentSemaphore.set(newNumber);
            logger.info("The Semaphores is modified to " + newNumber);
            return;
        }
    }
}
