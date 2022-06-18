package com.company.controllers;

import com.company.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class InstructorWithInstructorDetailWithCourseController {

    public static void createCourses() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try  {

            // start a transaction
            session.beginTransaction();

            // get the instructor from db
            int theId = 1;
            Instructor_1 tempInstructor = session.get(Instructor_1.class, theId);

            // create some courses
            Course tempCourse1 = new Course("Air Guitar - The Ultimate Guide");
            Course tempCourse2 = new Course("The Pinball Masterclass");

            // add courses to instructor
            tempInstructor.add(tempCourse1);
            tempInstructor.add(tempCourse2);

            // save the courses
            session.save(tempCourse1);
            session.save(tempCourse2);

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }

    public static void createInstructor1(String instructor1firstname, String instructor1lastname,
                                         String instructor1email, String instructordetail1youtubechannel,
                                         String instructordetail1hobby) {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // create the objects
            Instructor_1 tempInstructor =
                    new Instructor_1(instructor1firstname, instructor1lastname, instructor1email);

            InstructorDetail_1 tempInstructorDetail =
                    new InstructorDetail_1(
                            instructordetail1youtubechannel,
                            instructordetail1hobby);

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

            // add clean up code
            session.close();

            factory.close();
        }
    }

    public static void createDemo() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
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

            Instructor_1 tempInstructor =
                    new Instructor_1("Madhu", "Patel", "madhu@luv2code.com");

            InstructorDetail_1 tempInstructorDetail =
                    new InstructorDetail_1(
                            "http://www.youtube.com",
                            "Guitar");

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

    public static void deleteCourses() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get a course
            int theId = 10;
            Course tempCourse = session.get(Course.class, theId);

            // delete course
            System.out.println("Deleting course: " + tempCourse);

            session.delete(tempCourse);

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }

    public static void deleteInstructorDetail1() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the instructor detail object
            int theId = 3;
            InstructorDetail_1 tempInstructorDetail =
                    session.get(InstructorDetail_1.class, theId);

            // print the instructor detail
            System.out.println("tempInstructorDetail: " + tempInstructorDetail);

            // print  the associated instructor
            System.out.println("the associated instructor: " +
                    tempInstructorDetail.getInstructor());

            // now let's delete the instructor detail
            System.out.println("Deleting tempInstructorDetail: "
                    + tempInstructorDetail);

            // remove the associated object reference
            // break bi-directional link

            tempInstructorDetail.getInstructor().setInstructorDetail(null);

            session.delete(tempInstructorDetail);

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        finally {
            // handle connection leak issue
            session.close();

            factory.close();
        }
    }

    public static void deleteDemo() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get instructor by primary key / id
            int theId = 1;
            Instructor_1 tempInstructor =
                    session.get(Instructor_1.class, theId);

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

    public static void getInstructor1AndCourse() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the instructor from db
            int theId = 1;
            Instructor_1 tempInstructor = session.get(Instructor_1.class, theId);

            System.out.println("Instructor: " + tempInstructor);

            // get courses for the instructor
            System.out.println("Courses: " + tempInstructor.getCourses());

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }

    public static void getInstructor1AndInstructorDetail1() {

        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1.class)
                .addAnnotatedClass(InstructorDetail_1.class)
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the instructor detail object
            int theId = 1;
            InstructorDetail_1 tempInstructorDetail =
                    session.get(InstructorDetail_1.class, theId);

            // print the instructor detail
            System.out.println("tempInstructorDetail: " + tempInstructorDetail);

            // print  the associated instructor
            System.out.println("the associated instructor: " +
                    tempInstructorDetail.getInstructor());

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        finally {
            // handle connection leak issue
            session.close();

            factory.close();
        }
    }

}
