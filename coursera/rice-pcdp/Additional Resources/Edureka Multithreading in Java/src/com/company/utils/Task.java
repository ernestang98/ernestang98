package com.company.utils;

import java.util.UUID;
import java.util.logging.*;

import static com.company.utils.Service.print;

public class Task {

    private final String taskId;

    public Task() {
        this.taskId = UUID.randomUUID().toString();
        print(this.taskId, "creating task with id - ", this.taskId);
    }

    public void executeTask() {
        print(this.taskId, "executing task...");
        for (int i = 0; i < 2000; i++) {
            print(this.taskId, "iteration " + i);
        }
    }

}
