package com.rahul.fanatics.orchestration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ProducerRunnable implements Runnable {
    private final BlockingQueue<String> queue;
    private final AtomicInteger currentCount;
    private final int taskCount;
    private final Supplier<String> producerFunction;


    public ProducerRunnable(BlockingQueue<String> queue, AtomicInteger currentCount, int taskCount, Supplier<String> producerFunction) {
        this.queue = queue;
        this.currentCount = currentCount;
        this.taskCount = taskCount;
        this.producerFunction = producerFunction;
    }

    @Override
    public void run() {
        while (currentCount.get() < taskCount) {
            try {
                queue.put(producerFunction.get());
                currentCount.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
