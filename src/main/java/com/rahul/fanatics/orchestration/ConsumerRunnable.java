package com.rahul.fanatics.orchestration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import static com.rahul.fanatics.utils.Constants.POISON_PILL;

public class ConsumerRunnable<R> implements Runnable {
    private final BlockingQueue<String> queue;
    private final CountDownLatch latch;

    private final Function<String, R> consumerFunction;

    private final ResultAggregator<R> resultAggregator;

    private final int maxResultToCompute;

    public ConsumerRunnable(BlockingQueue<String> queue, CountDownLatch latch, Function<String, R> consumerFunction, ResultAggregator<R> resultAggregator, int maxResultToCompute) {
        this.queue = queue;
        this.latch = latch;
        this.consumerFunction = consumerFunction;
        this.resultAggregator = resultAggregator;
        this.maxResultToCompute = maxResultToCompute;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String seq = queue.take();
                if (POISON_PILL.equalsIgnoreCase(seq)) {
                    return;
                }
                resultAggregator.aggregateResult(consumerFunction.apply(seq), maxResultToCompute);
                latch.countDown();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
