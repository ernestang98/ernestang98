package com.example;

import java.util.List;

import com.example.dao.AccountDAO;
import com.example.model.Account;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class section_41_to_43 {

	public static void main(String[] args) {

		// read spring config java class
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(Config.class);
		
		// get the bean from spring container
		AccountDAO theAccountDAO = context.getBean("accountDAO", AccountDAO.class);
		
		// call method to find the accounts
		List<Account> theAccounts = theAccountDAO.findAccounts(false);
		
		// display the accounts
		System.out.println("\n\nMain Program: AfterReturningDemoApp");
		System.out.println("----");
		
		System.out.println(theAccounts);
		
		System.out.println("\n");

		try {
			// add a boolean flag to simulate exceptions
			boolean tripWire = true;
			theAccounts = theAccountDAO.findAccounts(tripWire);
		}
		catch (Exception exc) {
			System.out.println("\n\nMain Program ... caught exception: " + exc);
		}

		// display the accounts
		System.out.println("\n\nMain Program: AfterThrowingDemoApp");
		System.out.println("----");

		System.out.println(theAccounts);

		System.out.println("\n");

		
		// close the context
		context.close();
	}

}










