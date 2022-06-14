package com.company.controllers;

import com.company.models.Instructor;
import com.company.models.InstructorDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class InstructorController {

    public static void createInstructor(String instructorFirstName, String instructorLastName,
                                        String instructorEmail, String instructorDetailYoutube,
                                        String instructorDetailInstrument) {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(InstructorDetail.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // create the objects
			/*
			Instructor tempInstructor =
					new Instructor("Chad", "Darby", "darby@luv2code.com");

			InstructorDetail tempInstructorDetail =
					new InstructorDetail(
							"http://www.luv2code.com/youtube",
							"Luv 2 code!!!");
			*/

            Instructor tempInstructor =
                    new Instructor(instructorFirstName, instructorLastName, instructorEmail);

            InstructorDetail tempInstructorDetail =
                    new InstructorDetail(instructorDetailYoutube, instructorDetailInstrument);

            // associate the objects
            tempInstructor.setInstructorDetail(tempInstructorDetail);

            // start a transaction
            session.beginTransaction();

            // save the instructor
            //
            // Note: this will ALSO save the details object
            // because of CascadeType.ALL
            //
            System.out.println("Saving instructor: " + tempInstructor);
            session.save(tempInstructor);

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        finally {
            factory.close();
        }
    }

    public static void deleteInstructor(int theId) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor.class)
                .addAnnotatedClass(InstructorDetail.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {
            // start a transaction
            session.beginTransaction();

            // get instructor by primary key / id
            Instructor tempInstructor =
                    session.get(Instructor.class, theId);

            System.out.println("Found instructor: " + tempInstructor);

            // delete the instructors
            if (tempInstructor != null) {

                System.out.println("Deleting: " + tempInstructor);

                // Note: will ALSO delete associated "details" object
                // because of CascadeType.ALL
                //
                session.delete(tempInstructor);
            }

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        finally {
            factory.close();
        }
    }

}
