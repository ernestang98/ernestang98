package com.company;

import org.springframework.beans.factory.annotation.Value;

public class SwimCoach implements Coach {

    private ReminderService reminderService;

    @Value("${foo.email}")
    private String email;

    @Value("${foo.team}")
    private String team;

    public SwimCoach(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Override
    public String getDailyWorkout() {
        return "10 laps around the pool";
    }

    @Override
    public String getReminder() {
        return reminderService.getReminder();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
