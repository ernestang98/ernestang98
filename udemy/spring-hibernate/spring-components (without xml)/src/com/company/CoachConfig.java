package com.company;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// Basically acts as the applicationContext XML file
// @Component vs @Bean: @Component is for classes, @Bean is for methods
// @Configuration is for multiple @Bean
// https://howtodoinjava.com/spring-core/spring-configuration-annotation
// https://stackoverflow.com/questions/6827752/whats-the-difference-between-component-repository-service-annotations-in
// https://stackoverflow.com/questions/50558563/difference-between-component-scan-and-component
// https://www.baeldung.com/spring-component-annotation

@Configuration
@ComponentScan("com.company")
@PropertySource("classpath:sport.properties")
public class CoachConfig {
    @Bean
    public ReminderService reminderSleep8HoursService() {
        return new ReminderSleep8HoursService();
    }

    @Bean
    public Coach swimCoach() {
        return new SwimCoach(reminderSleep8HoursService());
    }

}
