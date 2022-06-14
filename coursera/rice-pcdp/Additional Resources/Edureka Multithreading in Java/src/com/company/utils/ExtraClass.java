package com.company.utils;

import static com.company.utils.Service.print;

public class ExtraClass {

    public void printDocuments(int numOfCopies, String documentName) throws InterruptedException {
        for (int i = 0; i < numOfCopies; i++) {
            Thread.sleep(50);
            print("EXTRA-CLASS-PRINTER-SERVICE", ">> printing " + documentName + " " + i);
        }
    }

//    public synchronized void printDocuments(int numOfCopies, String documentName) throws InterruptedException {
//        for (int i = 0; i < numOfCopies; i++) {
//            Thread.sleep(50);
//            print("EXTRA-CLASS-PRINTER-SERVICE", ">> printing " + documentName + " " + i);
//        }
//    }


}
