package com.company;

public class ReminderSleep8HoursService implements ReminderService {
    @Override
    public String getReminder() {
        return "Sleep 8 hours a day!";
    }
}
