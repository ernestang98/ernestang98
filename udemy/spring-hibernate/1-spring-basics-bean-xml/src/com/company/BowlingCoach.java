package com.company;

public class BowlingCoach implements GenericCoach {

    private FortuneService fortuneService;

    public BowlingCoach() {

    }

    public void setFortuneService(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @Override
    public String getDailyFortune() {
        return this.fortuneService.getFortune();
    }

    @Override
    public String getDailyWorkout() {
        return "10 Squats";
    }

}
