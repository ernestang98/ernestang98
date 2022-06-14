package com.company;

public class BadmintonCoach implements GenericCoach {

    public BadmintonCoach() {

    }

    @Override
    public String getDailyFortune() {
        return "Be content with life :3";
    }

    @Override
    public String getDailyWorkout() { return "10 Racket Swings"; }
}
