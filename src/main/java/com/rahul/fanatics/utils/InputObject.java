package com.rahul.fanatics.utils;

import com.beust.jcommander.Parameter;

import static com.rahul.fanatics.utils.Constants.TASK_TYPE_LONGEST_PREFIX;

public class InputObject {
    @Parameter(names = {"--taskInputQueueSize"}, description = "Size of shared queue to hold individual task input")
    public int taskInputQueueSize = 100;
    @Parameter(names = {"--producerThreadCount"}, description = "Producer parallelism, has to be one for deterministic results")
    public int producerThreadCount = 1;

    @Parameter(names = {"--consumerThreadCount"}, description = "Consumer parallelism, defaults to number of processors")
    public int consumerThreadCount = Runtime.getRuntime().availableProcessors();
    @Parameter(names = {"--taskCount"}, description = "Number of tasks to generate")
    public int taskCount;

    @Parameter(names = {"--seed"}, description = "Seed for random number generator")
    public int seed = 12345;
    @Parameter(names = {"--taskToRun"}, description = "Task to run , valid values are - LongestOnes and LongestPrefix ")
    public String taskToRun = TASK_TYPE_LONGEST_PREFIX;

    @Parameter(names = {"--isDeterministic"}, description = "Whether this run is deterministic so as to yield same result on multiple runs")
    public boolean isDeterministic = true;

    @Parameter(names = {"--queueSize"}, description = "Size of shared Queue ")
    public int queueSize = 100;

    @Parameter(names = {"--maxResultToCompute"}, description = "Max result to display> ")
    public int maxResultToCompute = 50;

    @Parameter(names = "--help", help = true)
    public boolean help = false;
}
