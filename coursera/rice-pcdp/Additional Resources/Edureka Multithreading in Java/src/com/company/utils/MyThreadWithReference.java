package com.company.utils;

import java.util.UUID;

import static com.company.utils.Service.print;

public class MyThreadWithReference extends Thread {

    private final String taskId;

    private ExtraClass ref;

    public MyThreadWithReference(ExtraClass ref) {
        this.taskId = UUID.randomUUID().toString();
        this.ref = ref;
        print(this.taskId, "creating thread referencing extra class with id - ", this.taskId);

    }

    @Override
    public void run() {
        print(this.taskId, "I am running...");
        try {

//            this.ref.printDocuments(1000, this.taskId.substring(1, 5) + ".pdf");

            synchronized (this.ref) {
                this.ref.printDocuments(1000, this.taskId.substring(1, 5) + ".pdf");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
