package com.company;

import org.springframework.beans.factory.DisposableBean;

public class ProfessionalSwimmingCoach implements GenericCoach, DisposableBean {

    private String linkedIn;
    private String highestAward;
    private FortuneService fortuneService;

    public ProfessionalSwimmingCoach() {

    }

    public void setFortuneService(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getHighestAward() {
        return highestAward;
    }

    public void setHighestAward(String highestAward) {
        this.highestAward = highestAward;
    }

    @Override
    public String getDailyFortune() {
        return this.fortuneService.getFortune();
    }

    @Override
    public String getDailyWorkout() { return "10 laps around the pool"; }

    public void initializeMethod() {
        System.out.println("|--Initialized--|");
    }

    public void destroyMethod() {
        System.out.println("/==Destroyed==/");
    }

    // Special Method to customize destroy method for ProtoType Beans
    @Override
    public void destroy() throws Exception {
        System.out.println("X== Special Destroyed==X");
    }

}
