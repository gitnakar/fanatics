package com.rahul.fanatics.tasks;


import com.rahul.fanatics.orchestration.ResultAggregator;
import com.rahul.fanatics.orchestration.TopNResultAggregator;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.rahul.fanatics.utils.Constants.SEQ_LENGTH;

public class LongestOnesTask implements Task<Integer> {

    private final ResultAggregator<Integer> resultAggregator = new TopNResultAggregator<>(Comparator.comparingInt(s -> s));

    @Override
    public Supplier<String> producerFunction(Random random) {
        return () -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < SEQ_LENGTH; i++) {
                sb.append(random.nextBoolean() ? "1" : "0");
            }
            return sb.toString();
        };
    }

    @Override
    public Function<String, Integer> transformationFunction() {
        return s -> {
            int start = -1;
            int longest = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '1') {
                    if (i == 0 || s.charAt(i - 1) == '0') {
                        start = i;
                    }
                } else {
                    // charAt(i) == 0
                    if (start > -1) {
                        longest = Math.max(longest, i - start);
                        start = -1;
                    }
                }
            }
            if (start == 0)
                return s.length();
            return longest;
        };
    }

    @Override
    public ResultAggregator<Integer> getResultAggregator() {
        return resultAggregator;
    }

    @Override
    public List<Integer> fetchComputedResult() {
        return resultAggregator.fetchComputedResult();
    }
}
