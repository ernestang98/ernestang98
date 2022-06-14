package com.company.controllers;

import com.company.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class CourseReviewController {

    public static void getCourseAndReviews(int index) {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1_Eager_Loading.class)
                .addAnnotatedClass(InstructorDetail_1_Eager_Loading.class)
                .addAnnotatedClass(Course_Eager_Loading.class)
                .addAnnotatedClass(Review.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the course
            int theId = index;
            Course_Eager_Loading tempCourse = session.get(Course_Eager_Loading.class, theId);

            // print the course
            System.out.println(tempCourse);

            // print the course reviews
            System.out.println(tempCourse.getReviews());

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

    public static void createCourseAndReviews() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1_Eager_Loading.class)
                .addAnnotatedClass(InstructorDetail_1_Eager_Loading.class)
                .addAnnotatedClass(Course_Eager_Loading.class)
                .addAnnotatedClass(Review.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();


            // create a course
            Course_Eager_Loading tempCourse = new Course_Eager_Loading("Pacman - How To Score One Million Points");

            // add some reviews
            tempCourse.addReview(new Review("Great course ... loved it!"));
            tempCourse.addReview(new Review("Cool course, job well done"));
            tempCourse.addReview(new Review("What a dumb course, you are an idiot!"));

            // save the course ... and leverage the cascade all :-)
            System.out.println("Saving the course");
            System.out.println(tempCourse);
            System.out.println(tempCourse.getReviews());

            session.save(tempCourse);

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

    public static void deleteCourseAndReviews(int index) {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Instructor_1_Eager_Loading.class)
                .addAnnotatedClass(InstructorDetail_1_Eager_Loading.class)
                .addAnnotatedClass(Course_Eager_Loading.class)
                .addAnnotatedClass(Review.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the course
            int theId = index;
            Course_Eager_Loading tempCourse = session.get(Course_Eager_Loading.class, theId);

            // print the course
            System.out.println("Deleting the course ... ");
            System.out.println(tempCourse);

            // print the course reviews
            System.out.println(tempCourse.getReviews());

            // delete the course
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
}
