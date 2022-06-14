package com.example;

import com.example.service.TrafficFortuneService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.logging.Logger;


public class section_44 {

	private static Logger myLogger =
			Logger.getLogger(section_44.class.getName());

	public static void main(String[] args) {

		// read spring config java class
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(Config.class);

		// get the bean from spring container
		TrafficFortuneService theFortuneService =
				context.getBean("trafficFortuneService", TrafficFortuneService.class);

		System.out.println("\nMain Program: AroundDemoApp");

		System.out.println("Calling getFortune");

		String data = theFortuneService.getFortune();

		System.out.println("\nMy fortune is: " + data);

		System.out.println("Finished");

		// get the bean from spring container
		theFortuneService =
				context.getBean("trafficFortuneService", TrafficFortuneService.class);

		myLogger.info("\nMain Program: AroundDemoApp");

		myLogger.info("Calling getFortune");

		boolean tripWire = true;
		data = theFortuneService.getFortune(tripWire);

		myLogger.info("\nMy fortune is: " + data);

		myLogger.info("Finished");

		// get the bean from spring container
		theFortuneService =
				context.getBean("trafficFortuneService", TrafficFortuneService.class);

		myLogger.info("\nMain Program: AroundDemoApp");

		myLogger.info("Calling getFortune");

		data = theFortuneService.getFortune();

		myLogger.info("\nMy fortune is: " + data);

		myLogger.info("Finished");

		// close the context
		context.close();
	}

}










