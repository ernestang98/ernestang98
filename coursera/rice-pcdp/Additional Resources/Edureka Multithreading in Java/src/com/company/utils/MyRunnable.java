package com.company.utils;

import java.util.UUID;

import static com.company.utils.Service.print;

public class MyRunnable extends ExtraClass implements Runnable {

    private final String taskId;

    public MyRunnable() {
        this.taskId = UUID.randomUUID().toString();
        print(this.taskId, "creating runnable with id - ", this.taskId);

    }

    @Override
    public void run() {
        print(this.taskId, "I am running...");
        this.executeTask();
    }

    private void executeTask() {
        for (int i = 0; i < 1000; i++) {
            print(this.taskId, "iteration - " + i);
        }
    }

}
