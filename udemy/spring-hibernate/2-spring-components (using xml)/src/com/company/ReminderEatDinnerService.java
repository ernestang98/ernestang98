package com.company;

import org.springframework.stereotype.Component;

@Component
public class ReminderEatDinnerService implements ReminderService {
    @Override
    public String getReminder() {
        return "Please eat your dinner!";
    }
}
