package com.rahul.fanatics.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LongestPrefixTaskTest {

    @Test
    @DisplayName("Given an input string with valid prefix, returns the prefix")
    void testScenario1() {
        Task<String> longestPrefixTask = new LongestPrefixTask();
        String result = longestPrefixTask.transformationFunction().apply("TAATAAGGCCTCCC");
        Assertions.assertEquals(result, "AAGGCCT");
    }
}