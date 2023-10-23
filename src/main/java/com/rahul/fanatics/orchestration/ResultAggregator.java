package com.rahul.fanatics.orchestration;

import java.util.List;

public interface ResultAggregator<R> {

    void aggregateResult(R r, int numResultToHold);

    List<R> fetchComputedResult();
}
