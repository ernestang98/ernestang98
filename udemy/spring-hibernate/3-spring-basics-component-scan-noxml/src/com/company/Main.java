package com.company;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(CoachConfig.class);

        SwimCoach theCoach = context.getBean("swimCoach", SwimCoach.class);

        // call a method on the bean
        System.out.println(theCoach.getDailyWorkout());
        System.out.println(theCoach.getReminder());
        System.out.println(theCoach.getEmail());
        System.out.println(theCoach.getTeam());

        // close the context
        context.close();

    }
}
