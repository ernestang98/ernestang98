package com.company;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        // read spring config file
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        // get the bean from spring container

        // Explicit Component Names
//        Coach theCoach = context.getBean("tennisCoachComponent", Coach.class);

        // Default Component Names
        TennisCoach theCoach = context.getBean("tennisCoach", TennisCoach.class);

        // call a method on the bean
        System.out.println(theCoach.getDailyWorkout());
        System.out.println(theCoach.getReminder());
        System.out.println(theCoach.getEmail());
        System.out.println(theCoach.getTeam());

        TennisCoach theCoachAlso = context.getBean("tennisCoach", TennisCoach.class);

        System.out.println(theCoach == theCoachAlso);

        // close the context
        context.close();

    }
}
