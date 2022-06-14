package com.company.controllers;

import com.company.models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

// Hibernate Query Language (HQL) used here

public class StudentController {

    public static void createStudent(String firstname, String lastname, String email) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            Student temp = new Student(firstname, lastname, email);
            session.beginTransaction();
            session.save(temp);
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }

    public static void readStudent(Integer id) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();
            Student myStudent = session.get(Student.class, id);
            System.out.println("Get complete: " + myStudent);
            System.out.println("Done!");
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }

    public static void queryStudent() {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            List<Student> theStudents = session.createQuery("SELECT s FROM Student s").getResultList();
            displayStudents(theStudents);
            theStudents = session.createQuery("from Student s where s.lastName='Doe'").getResultList();
            System.out.println("\n\nStudents who have last name of Doe");
            displayStudents(theStudents);
            theStudents =
                    session.createQuery("from Student s where"
                            + " s.lastName='Doe' OR s.firstName='Daffy'").getResultList();
            System.out.println("\n\nStudents who have last name of Doe OR first name Daffy");
            displayStudents(theStudents);
            theStudents = session.createQuery("from Student s where"
                    + " s.email LIKE '%gmail.com'").getResultList();

            System.out.println("\n\nStudents whose email ends with gmail.com");
            displayStudents(theStudents);
            session.getTransaction().commit();
            System.out.println("Done!");
        }
        finally {
            factory.close();
        }
    }

    public static void updateStudents(int studentId) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            System.out.println("\nGetting student with id: " + studentId);
            Student myStudent = session.get(Student.class, studentId);
            System.out.println("Updating student...");
            myStudent.setFirstName("Scooby");
            session.getTransaction().commit();
            session = factory.getCurrentSession();
            session.beginTransaction();

            System.out.println("Update email for all students");
            session.createQuery("update Student set email='foo@gmail.com'")
                    .executeUpdate();

            session.getTransaction().commit();
            System.out.println("Done!");
        }
        finally {
            factory.close();
        }
    }

    public static void deleteStudent(int id) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            System.out.println("\nGetting student with id: " + id);

            Student myStudent = session.get(Student.class, id);

//             delete the student
             System.out.println("Deleting student: " + myStudent);
             session.delete(myStudent);

            // delete student id=2
            System.out.println("Deleting student id=2");

            session.createQuery("delete from Student where id=2").executeUpdate();

            // commit the transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        finally {
            factory.close();
        }
    }

    private static void displayStudents(List<Student> theStudents) {
        for (Student tempStudent : theStudents) {
            System.out.println(tempStudent);
        }
    }

}
