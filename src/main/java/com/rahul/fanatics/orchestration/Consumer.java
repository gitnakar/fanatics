package com.rahul.fanatics.orchestration;

import com.rahul.fanatics.tasks.Task;
import com.rahul.fanatics.utils.InputObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Consumer<R> {

    private final BlockingQueue<String> queue;
    private final CountDownLatch latch;
    private final Function<String, R> consumerFunction;
    private final int consumerThreadCount;
    private final ExecutorService consumerPool;

    private final ResultAggregator<R> resultAggregator;

    private final int maxResultToCompute;

    public Consumer(InputObject inputObject, BlockingQueue<String> queue, Task<R> task, CountDownLatch latch) {
        this.queue = queue;
        this.latch = latch;
        this.consumerThreadCount = inputObject.consumerThreadCount;
        this.consumerFunction = task.transformationFunction();
        this.maxResultToCompute = inputObject.maxResultToCompute;
        this.consumerPool = Executors.newFixedThreadPool(consumerThreadCount);
        this.resultAggregator = task.getResultAggregator();
    }

    public void start() {
        IntStream.range(0, consumerThreadCount).mapToObj(i -> new ConsumerRunnable<R>(queue, latch, consumerFunction, resultAggregator, maxResultToCompute)).forEach(consumerPool::submit);
    }

    public void shutDown() {
        consumerPool.shutdownNow();
    }
}
