package com.rahul.fanatics;

import com.beust.jcommander.JCommander;
import com.rahul.fanatics.orchestration.Consumer;
import com.rahul.fanatics.orchestration.Producer;
import com.rahul.fanatics.tasks.Task;
import com.rahul.fanatics.utils.InputObject;
import com.rahul.fanatics.utils.TaskType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.rahul.fanatics.utils.Constants.POISON_PILL;
import static java.lang.System.out;


public class EntryPoint {

    private final AtomicInteger currentCount = new AtomicInteger(0);
    InputObject inputObject;
    private BlockingQueue<String> queue;
    private Task<?> task;
    private CountDownLatch consumerLatch;
    private Producer producer;
    private Consumer<?> consumer;


    public static void main(String[] args) throws Exception {
        EntryPoint entryPoint = new EntryPoint();
        InputObject inputObject = new InputObject();
        if (args[0].contains("--")) {
            new JCommander(inputObject).parse(args);
        } else {
            inputObject.taskToRun = args[0];
            inputObject.taskCount = Integer.parseInt(args[1]);
            inputObject.seed = Integer.parseInt(args[2]);
        }
        entryPoint.inputObject = inputObject;
        entryPoint.runSimulation().forEach(out::println);
    }

    private void validateInput() {
        if (inputObject.isDeterministic && inputObject.producerThreadCount > 1)
            throw new IllegalArgumentException("For a deterministic run , you can only have 1 producer thread");
    }

    List<String> runSimulation() throws InterruptedException {
        computeDerivedVariables();
        validateInput();
        initializeTask();
        initializeSharedQueue();
        startProducer();
        startConsumer();
        waitForAllTasksToGetScheduled();
        injectPoisonPill();
        waitForAllTasksToGetConsumed();
        shutDownThreadPools();
        return fetchOutput();
    }

    private void computeDerivedVariables() {
        queue = new LinkedBlockingQueue<>(inputObject.queueSize);
        consumerLatch = new CountDownLatch(inputObject.taskCount);
    }

    private void shutDownThreadPools() {
        producer.shutDown();
        consumer.shutDown();
    }

    private void initializeTask() {
        task = Arrays.stream(TaskType.values()).sequential().filter(taskType -> inputObject.taskToRun.equalsIgnoreCase(taskType.getName())).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid value for task name")).getTask();
    }

    private void initializeSharedQueue() {
        queue = new LinkedBlockingQueue<>(inputObject.queueSize);
    }

    private List<String> fetchOutput() {
        return task.fetchComputedResult().stream().map(Object::toString).collect(Collectors.toList());
    }

    private void waitForAllTasksToGetConsumed() {
        try {
            consumerLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectPoisonPill() {
        for (int i = 0; i < inputObject.consumerThreadCount; i++) {
            try {
                queue.put(POISON_PILL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void waitForAllTasksToGetScheduled() throws InterruptedException {
        while (currentCount.get() < inputObject.taskCount) {
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }

    private void startConsumer() {
        consumerLatch = new CountDownLatch(inputObject.taskCount);
        consumer = new Consumer<>(inputObject, queue, task, consumerLatch);
        consumer.start();
    }

    private void startProducer() {
        producer = new Producer(inputObject, task, queue, currentCount);
        producer.start();
    }

}
