package com.rahul.fanatics.utils;

import com.rahul.fanatics.tasks.LongestOnesTask;
import com.rahul.fanatics.tasks.LongestPrefixTask;
import com.rahul.fanatics.tasks.Task;

import static com.rahul.fanatics.utils.Constants.TASK_TYPE_LONGEST_ONES;
import static com.rahul.fanatics.utils.Constants.TASK_TYPE_LONGEST_PREFIX;

public enum TaskType {
    LONGEST_ONES(TASK_TYPE_LONGEST_ONES, new LongestOnesTask()),
    LONGEST_PREFIX(TASK_TYPE_LONGEST_PREFIX, new LongestPrefixTask());

    final String name;
    final Task<?> task;

    TaskType(String name, Task<?> task) {
        this.name = name;
        this.task = task;
    }

    public Task<?> getTask() {
        return this.task;
    }

    public String getName() {
        return this.name;
    }
}
