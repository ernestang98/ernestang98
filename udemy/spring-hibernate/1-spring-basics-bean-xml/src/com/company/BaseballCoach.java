package com.company;

public class BaseballCoach implements GenericCoach {

    private final FortuneService fortuneService;

    public BaseballCoach(FortuneService theFortuneService) {
        fortuneService = theFortuneService;
    }

    @Override
    public String getDailyFortune() {
        return this.fortuneService.getFortune();
    }

    @Override
    public String getDailyWorkout() {
        return "10 Push Ups";
    }


}
