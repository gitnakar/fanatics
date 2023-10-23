package com.rahul.fanatics.tasks;

import com.rahul.fanatics.orchestration.ResultAggregator;
import com.rahul.fanatics.orchestration.TopNResultAggregator;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.rahul.fanatics.utils.Constants.*;

public class LongestPrefixTask implements Task<String> {

    ResultAggregator<String> resultAggregator = new TopNResultAggregator<>(Comparator.comparingInt(String::length));

    @Override
    public Supplier<String> producerFunction(Random random) {
        return () -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < SEQ_LENGTH; i++) {
                sb.append(PREFIX_CHARS[random.nextInt(PREFIX_CHARS.length)]);
            }
            return sb.toString();
        };
    }

    @Override
    public Function<String, String> transformationFunction() {
        return seq -> {
            int currentInputIndex = 0;
            int longest = 0;
            for (int i = 0; i < seq.length(); i++) {
                if (seq.charAt(i) == INPUT_STRING.charAt(currentInputIndex)) {
                    currentInputIndex++;
                } else {
                    longest = Math.max(currentInputIndex, longest);
                    currentInputIndex = 0;
                }
            }
            if (currentInputIndex > 0)
                longest = Math.max(currentInputIndex, longest);
            return INPUT_STRING.substring(0, longest);
        };
    }

    public ResultAggregator<String> getResultAggregator() {
        return resultAggregator;
    }

    @Override
    public List<String> fetchComputedResult() {
        return resultAggregator.fetchComputedResult();
    }

}
