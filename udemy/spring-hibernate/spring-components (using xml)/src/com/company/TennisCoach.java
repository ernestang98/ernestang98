package com.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;


//@Component("tennisCoachComponent")
//@Scope default: Singleton
//Same Rule applies as seen in springXML
//@Scope("prototype")
@Component
public class TennisCoach implements Coach {

	// Field Injection
//	@Autowired
	private ReminderService reminderService;

	@Value("${foo.email}")
	private String email;

	@Value("${foo.team}")
	private String team;

	public TennisCoach() {

	}

	@PostConstruct
	public void init() {
		System.out.println("Initializing...");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Destroying...");
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

	// Setter Injection
	@Autowired
	// When defining qualifier, can also put on top of the constructor
//	@Qualifier("REMINDERRandomAdviceService")
	public void thisCanBeAnything(@Qualifier("REMINDERRandomAdviceService")
			ReminderService theReminderService) {
		reminderService = theReminderService;
	}

	// Constructor Injection
//	@Autowired
//	public TennisCoach(ReminderService theReminderService) {
//		reminderService = theReminderService;
//	}

	@Override
	public String getDailyWorkout() {
		return "Practice your backhand volley";
	}

	@Override
	public String getReminder() {
		return reminderService.getReminder();
	}

}
