package com.rahul.fanatics.orchestration;

import com.rahul.fanatics.tasks.Task;
import com.rahul.fanatics.utils.InputObject;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;


public class Producer {

    private final InputObject inputObject;

    private final AtomicInteger currentCount;

    private final ExecutorService producerPool;

    private final Supplier<String> producerFunction;

    private final BlockingQueue<String> queue;


    public Producer(InputObject inputObject, Task<?> task, BlockingQueue<String> queue, AtomicInteger currentCount) {
        this.inputObject = inputObject;
        this.currentCount = currentCount;
        Random random = new Random(inputObject.seed);
        this.producerPool = Executors.newFixedThreadPool(inputObject.producerThreadCount);
        this.producerFunction = task.producerFunction(random);
        this.queue = queue;
    }

    public void start() {
        IntStream.range(0, inputObject.producerThreadCount).mapToObj(i -> new ProducerRunnable(queue, currentCount, inputObject.taskCount, producerFunction)).forEach(producerPool::submit);
    }

    public void shutDown() {
        producerPool.shutdownNow();
    }

}
