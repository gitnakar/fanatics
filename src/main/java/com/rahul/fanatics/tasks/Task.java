package com.rahul.fanatics.tasks;

import com.rahul.fanatics.orchestration.ResultAggregator;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Task<R> {

    Supplier<String> producerFunction(Random random);

    Function<String, R> transformationFunction();

    ResultAggregator<R> getResultAggregator();

    List<R> fetchComputedResult();
}
