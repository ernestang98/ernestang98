package com.company.utils;

public class Service {
    public static void print(String id, String... args) {
        System.out.println(id + ": " + String.join(" ", args));
    }
}
