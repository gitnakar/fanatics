package com.rahul.fanatics.orchestration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class TopNResultAggregator<R> implements ResultAggregator<R> {
    private final PriorityBlockingQueue<R> heap;


    public TopNResultAggregator(Comparator<R> comparator) {
        heap = new PriorityBlockingQueue<R>(5, comparator);
    }

    @Override
    public void aggregateResult(R r, int numResultToHold) {
        heap.add(r);
        if (heap.size() > numResultToHold) {
            synchronized (heap) {
                while (heap.size() > numResultToHold) {
                    heap.remove();
                }
            }
        }
    }

    public List<R> fetchComputedResult() {
        List<R> result = new ArrayList<>();
        while (heap.size() > 0) {
            result.add(heap.remove());
        }
        Collections.reverse(result);
        return result;
    }
}
