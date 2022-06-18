package com.company;

import org.springframework.stereotype.Component;

@Component
public class ReminderWashHandsService implements ReminderService {
    @Override
    public String getReminder() {
        return "Wash your hands 3 times a day";
    }
}
