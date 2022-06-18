package com.company;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class REMINDERRandomAdviceService implements ReminderService {

    private String[] randomData = {
            "Shit 2 times a day",
            "Brush your teeth 3 times a day"
    };

    private Random randomNumber = new Random();

    @Override
    public String getReminder() {
        int index = randomNumber.nextInt(randomData.length);
        return randomData[index];
    }
}
