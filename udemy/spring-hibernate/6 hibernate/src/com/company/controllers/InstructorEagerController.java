package com.company.controllers;

import com.company.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class InstructorEagerController {

    public static void easyLazyDemo() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1_Eager_Loading.class)
                .addAnnotatedClass(InstructorDetail_1_Eager_Loading.class)
                .addAnnotatedClass(Course_Eager_Loading.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the instructor from db
            int theId = 1;
            Instructor_1_Eager_Loading tempInstructor = session.get(Instructor_1_Eager_Loading.class, theId);

            System.out.println("luv2code: Instructor: " + tempInstructor);

            // Solution 1 for Lazy Loading Error
            System.out.println("luv2code: Courses: " + tempInstructor.getCourses());

            // commit transaction
            session.getTransaction().commit();

            // close the session
            session.close();

            System.out.println("\nluv2code: The session is now closed!\n");

            // option 1: call getter method while session is open

            // get courses for the instructor
            System.out.println("luv2code: Courses: " + tempInstructor.getCourses());

            System.out.println("luv2code: Done!");
        }
        finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }

    public static void fetchJoinDemo() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1_Eager_Loading.class)
                .addAnnotatedClass(InstructorDetail_1_Eager_Loading.class)
                .addAnnotatedClass(Course_Eager_Loading.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // option 2: Hibernate query with HQL

            // get the instructor from db
            int theId = 1;

            Query<Instructor_1_Eager_Loading> query =
                    session.createQuery("select i from Instructor_1_Eager_Loading i "
                                    + "JOIN FETCH i.courses "
                                    + "where i.id=:theInstructorId",
                            Instructor_1_Eager_Loading.class);

            // set parameter on query
            query.setParameter("theInstructorId", theId);

            // execute query and get instructor
            Instructor_1_Eager_Loading tempInstructor = query.getSingleResult();

            System.out.println("luv2code: Instructor: " + tempInstructor);

            // commit transaction
            session.getTransaction().commit();

            // close the session
            session.close();

            System.out.println("\nluv2code: The session is now closed!\n");

            // get courses for the instructor
            System.out.println("luv2code: Courses: " + tempInstructor.getCourses());

            System.out.println("luv2code: Done!");
        }
        finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }

    public static void getCoursesLater() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1_Eager_Loading.class)
                .addAnnotatedClass(InstructorDetail_1_Eager_Loading.class)
                .addAnnotatedClass(Course_Eager_Loading.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the instructor from db
            int theId = 1;
            Instructor_1_Eager_Loading tempInstructor = session.get(Instructor_1_Eager_Loading.class, theId);

            System.out.println("luv2code: Instructor: " + tempInstructor);

            // commit transaction
            session.getTransaction().commit();

            // close the session
            session.close();
            System.out.println("\nluv2code: The session is now closed!\n");
            //
            // THIS HAPPENS SOMEWHERE ELSE / LATER IN THE PROGRAM
            // YOU NEED TO GET A NEW SESSION
            //

            System.out.println("\n\nluv2code: Opening a NEW session \n");
            session = factory.getCurrentSession();

            session.beginTransaction();

            // get courses for a given instructor
            Query<Course_Eager_Loading> query = session.createQuery("select c from Course_Eager_Loading c "
                            + "where c.instructorTEST.id=:theInstructorId",
                    Course_Eager_Loading.class);

            query.setParameter("theInstructorId", theId);

            List<Course_Eager_Loading> tempCourses = query.getResultList();

            System.out.println("tempCourses: " + tempCourses);

            // now assign to instructor object in memory
            tempInstructor.setCourses(tempCourses);

            System.out.println("luv2code: Courses: " + tempInstructor.getCourses());

            session.getTransaction().commit();

            System.out.println("luv2code: Done!");
        }
        finally {

            // add clean up code
            session.close();

            factory.close();
        }
    }

}
