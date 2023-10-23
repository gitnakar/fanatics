package com.rahul.fanatics.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class LongestOnesTaskTest {

    @Test
    @DisplayName("Given string 01011001110 should return 3")
    void testScenario1() {
        Task<Integer> longestOnesTask = new LongestOnesTask();
        int result = longestOnesTask.transformationFunction().apply("01011001110");
        Assertions.assertEquals(result, 3);
    }

    @Test
    @DisplayName("Given string 010100 should return 1")
    void testScenario2() {
        Task<Integer> longestOnesTask = new LongestOnesTask();
        int result = longestOnesTask.transformationFunction().apply("010100");
        Assertions.assertEquals(result, 1);
    }

    @Test
    @DisplayName("Given string 00000000 should return 0")
    void testScenario3() {
        Task<Integer> longestOnesTask = new LongestOnesTask();
        int result = longestOnesTask.transformationFunction().apply("00000000");
        Assertions.assertEquals(result, 0);
    }

    @Test
    @DisplayName("Given string 11111111 should return length of string")
    void testScenario4() {
        Task<Integer> longestOnesTask = new LongestOnesTask();
        String seq = "11111111";
        int result = longestOnesTask.transformationFunction().apply(seq);
        Assertions.assertEquals(result, seq.length());
    }
}