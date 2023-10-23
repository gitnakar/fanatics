package com.rahul.fanatics;

import com.rahul.fanatics.utils.InputObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class EntryPointTest {

    @Test
    @DisplayName("Test positive scenario")
    void scenario1() throws InterruptedException {
        runSimulation().forEach(System.out::println);
    }

    @Test
    @DisplayName("Running twice with same seed is deterministic")
    void scenario2() throws InterruptedException {
        List<String> result = runSimulation();
        List<String> result2 = runSimulation();
        Assertions.assertIterableEquals(result2,result);
    }

    @Test
    @DisplayName("Running with 2 producer threads will give non-deterministic result")
    void scenario3() throws InterruptedException {
        InputObject inputObject = getInputObject();
        inputObject.isDeterministic = false;
        inputObject.producerThreadCount = 2;
        EntryPoint entryPoint = new EntryPoint();
        entryPoint.inputObject = inputObject;
        List<String> result = entryPoint.runSimulation();
        EntryPoint entryPoint1 = new EntryPoint();
        entryPoint1.inputObject = inputObject;
        List<String> result2 = entryPoint1.runSimulation();
        Assertions.assertFalse(isListEqual(result,result2));
    }

    private boolean isListEqual(List<String> result, List<String> result2) {
        if (result.size() != result2.size())
            return false;
        for (int i = 0; i < result.size(); i++) {
            if(!result.get(i).equalsIgnoreCase(result2.get(i)))
                return false;
        }
        return true;
    }

    private List<String> runSimulation() throws InterruptedException {
        InputObject inputObject = getInputObject();
        EntryPoint entryPoint = new EntryPoint();
        entryPoint.inputObject = inputObject;
        return entryPoint.runSimulation();
    }

    private static InputObject getInputObject() {
        InputObject inputObject = new InputObject();
        inputObject.taskToRun = "LongestPrefix";
        inputObject.taskCount = 10;
        inputObject.seed = 12345;
        inputObject.maxResultToCompute = 5;
        return inputObject;
    }
}