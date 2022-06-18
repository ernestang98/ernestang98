package com.company;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        // Best Practice + Inversion of Control
        GenericCoach coach = new BowlingCoach();
        String workout = coach.getDailyWorkout();
        System.out.println(workout);

        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        // Badminton Coach with no injections
        GenericCoach coach0 = context.getBean("myCoach0", GenericCoach.class);
        System.out.println(coach0.getDailyWorkout());
        System.out.println(coach0.getDailyFortune());
        System.out.println();

        // Baseball Coach with Dependency Injection
        GenericCoach coach1 = context.getBean("myCoach1", GenericCoach.class);
        System.out.println(coach1.getDailyWorkout());
        System.out.println(coach1.getDailyFortune());
        System.out.println();

        // Bowling Coach with Setter Injection
        GenericCoach coach2 = context.getBean("myCoach2", GenericCoach.class);
        System.out.println(coach2.getDailyWorkout());
        System.out.println(coach2.getDailyFortune());
        System.out.println();

        // Professional Coach with Literal Value Injection
        ProfessionalSwimmingCoach coach3 = context.getBean("myCoach3", ProfessionalSwimmingCoach.class);
        System.out.println(coach3.getDailyWorkout());
        System.out.println(coach3.getDailyFortune());
        System.out.println(coach3.getHighestAward());
        System.out.println(coach3.getLinkedIn());
        System.out.println();

        // Professional Coach with .properties Injection
        ProfessionalSwimmingCoach coach4 = context.getBean("myCoach4", ProfessionalSwimmingCoach.class);
        System.out.println(coach4.getHighestAward());
        System.out.println(coach4.getLinkedIn());
        System.out.println();

        // Singleton Scope vs ProtoType Scope
        // https://stackoverflow.com/questions/16058365/what-is-difference-between-singleton-and-prototype-bean
        // Init & Destroy Methods
        // Init Method called at the start of Bean lifecycle, when Bean factory is creating all of the beans
        // Destroy Method called at the end of Bean lifecycle, i.e. context.close()
        // Destroyed not called on ProtoType Scoped objects
        // https://stackoverflow.com/questions/16783552/destroy-method-is-not-working-in-spring-framework
        ProfessionalSwimmingCoach coach41 = context.getBean("myCoach4", ProfessionalSwimmingCoach.class);
        System.out.println(coach41 == coach4);
        System.out.println(coach4);
        System.out.println(coach41);
        System.out.println();

        ProfessionalSwimmingCoach coach5 = context.getBean("myCoach5", ProfessionalSwimmingCoach.class);
        ProfessionalSwimmingCoach coach51 = context.getBean("myCoach5", ProfessionalSwimmingCoach.class);
        System.out.println(coach51 == coach5);
        System.out.println(coach5);
        System.out.println(coach51);
        System.out.println();

        context.close();
    }
    
}
