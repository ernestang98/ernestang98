package com.company;

import com.company.utils.ExtraClass;
import com.company.utils.MyThreadWithReference;

import static com.company.utils.Service.print;

// EDUREKA TUTORIAL LINK: https://www.youtube.com/watch?v=TCd8QIS-2KI&t=708s
public class MultiThreadingTutorial {

    public void run() throws InterruptedException {
        long startingTime = System.currentTimeMillis();
        print("MAIN", "==== APPLICATION STARTED ====");

        // Running on one thread
//        Task task1 = new Task();
//        Task task2 = new Task();
//        task1.executeTask();
//        task2.executeTask();

        // What about running multiple threads?
        // Lifecycle of thread starts with .start() method
        // .start() auto triggers the .run() command
        // thread2 does not wait for thread1 to finish, likewise, MAIN's iteration does not have for thread2 to finish
        // new MyThread() are child/worker threads from the MAIN class parent thread
        // MAIN, thread1, thread2 are running concurrently
//        MyThread thread1 = new MyThread();
//        MyThread thread2 = new MyThread();
//        thread1.start();
//        thread2.start();

        // what if our class is already inheriting from a parent class? Multi inheritance is not allowed in Java!
        // We use Runnable!
//        MyRunnable runnable1 = new MyRunnable();
//        Thread thread1 = new Thread(runnable1);
//        thread1.start();
//
//        MyRunnable runnable2 = new MyRunnable();
//        Thread thread2 = new Thread(runnable2);
//        thread2.start();

//        for (int i = 0; i < 20; i++) {
//            print("MAIN", "iteration " + i);
//        }

        // There are 2 ways to apply synchronization with the synchronize keyword, see ExtraClass.java and MyThreadWithReference.java
        // If you use 2 separate extraCLass, you cannot synchronize the threads via synchronize, you have to use the .join() method
        // With .join(), thread2 will only start after thread1 has finished!
        
        // Using one extraClass
        ExtraClass extraClass = new ExtraClass();
        MyThreadWithReference thread1 = new MyThreadWithReference(extraClass);
        MyThreadWithReference thread2 = new MyThreadWithReference(extraClass);

        // Using two extraClass
//        ExtraClass extraClass1 = new ExtraClass();
//        ExtraClass extraClass2 = new ExtraClass();
//        MyThreadWithReference thread1 = new MyThreadWithReference(extraClass1);
//        MyThreadWithReference thread2 = new MyThreadWithReference(extraClass2);

        thread1.start();
//        thread1.join();
        thread2.start();

        print("MAIN", "%%%% APPLICATION ENDED %%%%");
        print("MAIN", "Ending time: " + (System.currentTimeMillis() - startingTime));
    }

}
