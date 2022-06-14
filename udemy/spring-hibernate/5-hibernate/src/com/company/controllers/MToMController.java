package com.company.controllers;

import com.company.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MToMController {
    public static void addCoursesToStudent() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the student mary from database
            int studentId = 3;
            M_TO_M_STUDENT tempStudent = session.get(M_TO_M_STUDENT.class, studentId);

            System.out.println("\nLoaded student: " + tempStudent);
            System.out.println("Courses: " + tempStudent.getCourses());

            // create more courses
            M_TO_M_COURSE tempCourse1 = new M_TO_M_COURSE("Rubik's Cube - How to Speed Cube");
            M_TO_M_COURSE tempCourse2 = new M_TO_M_COURSE("Atari 2600 - Game Development");

            // add student to courses
            tempCourse1.addStudent(tempStudent);
            tempCourse2.addStudent(tempStudent);

            // save the courses
            System.out.println("\nSaving the courses ...");

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
    public static void createCourseAndReview() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();


            // create a course
            M_TO_M_COURSE tempCourse = new M_TO_M_COURSE("Pacman - How To Score One Million Points");

            // add some reviews
            tempCourse.addReview(new M_TO_M_REVIEW("Great course ... loved it!"));
            tempCourse.addReview(new M_TO_M_REVIEW("Cool course, job well done"));
            tempCourse.addReview(new M_TO_M_REVIEW("What a dumb course, you are an idiot!"));

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
    public static void createCourseAndStudent() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // create a course
            M_TO_M_COURSE tempCourse = new M_TO_M_COURSE("Pacman - How To Score One Million Points");

            // save the course
            System.out.println("\nSaving the course ...");
            session.save(tempCourse);
            System.out.println("Saved the course: " + tempCourse);

            // create the students
            M_TO_M_STUDENT tempStudent1 = new M_TO_M_STUDENT("John", "Doe", "john@luv2code.com");
            M_TO_M_STUDENT tempStudent2 = new M_TO_M_STUDENT("Mary", "Public", "mary@luv2code.com");

            // add students to the course
            tempCourse.addStudent(tempStudent1);
            tempCourse.addStudent(tempStudent2);

            // save the students
            System.out.println("\nSaving students ...");
            session.save(tempStudent1);
            session.save(tempStudent2);
            System.out.println("Saved students: " + tempCourse.getStudents());

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
    public static void deleteStudent() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the student from database
            int studentId = 1;
            M_TO_M_STUDENT tempStudent = session.get(M_TO_M_STUDENT.class, studentId);

            System.out.println("\nLoaded student: " + tempStudent);
            System.out.println("Courses: " + tempStudent.getCourses());

            // delete student
            System.out.println("\nDeleting student: " + tempStudent);
            session.delete(tempStudent);

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
    public static void deleteCourse() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the pacman course from db
            int courseId = 13;
            M_TO_M_COURSE tempCourse = session.get(M_TO_M_COURSE.class, courseId);

            // delete the course
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
    public static void getCourses() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {

            // start a transaction
            session.beginTransaction();

            // get the student from database
            int studentId = 1;
            M_TO_M_STUDENT tempStudent = session.get(M_TO_M_STUDENT.class, studentId);

            System.out.println("\nLoaded student: " + tempStudent);
            System.out.println("Courses: " + tempStudent.getCourses());

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
    public static void createStudentsOnly() {
        // create session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(M_TO_M_INSTRUCTOR.class)
                .addAnnotatedClass(M_TO_M_INSTRUCTORDETAIL.class)
                .addAnnotatedClass(M_TO_M_COURSE.class)
                .addAnnotatedClass(M_TO_M_REVIEW.class)
                .addAnnotatedClass(M_TO_M_STUDENT.class)
                .buildSessionFactory();

        // create session
        Session session = factory.getCurrentSession();

        try {
            // start a transaction
            session.beginTransaction();

            // create the students
            M_TO_M_STUDENT tempStudent1 = new M_TO_M_STUDENT("John", "Doe", "john@luv2code.com");
            M_TO_M_STUDENT tempStudent2 = new M_TO_M_STUDENT("Mary", "Public", "mary@luv2code.com");

            // save the students
            System.out.println("\nSaving students ...");
            session.save(tempStudent1);
            session.save(tempStudent2);

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

