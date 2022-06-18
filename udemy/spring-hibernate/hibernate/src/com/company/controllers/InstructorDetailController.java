package com.company.controllers;

import com.company.models.Instructor;
import com.company.models.InstructorDetail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class InstructorDetailController {

    public static void GetInstructorDetail() {
        // create session factory
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

            // get the instructor detail object
            int theId = 1;
            InstructorDetail tempInstructorDetail =
                    session.get(InstructorDetail.class, theId);

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

    public static void DeleteInstructorDetail(int theId) {

        // create session factory
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

            // get the instructor detail object
            InstructorDetail tempInstructorDetail =
                    session.get(InstructorDetail.class, theId);

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
            // set the Instructor property to null, so that we can properly delete the detail without deleting the instructor
            tempInstructorDetail.getInstructor().setInstructorDetail(null);

            session.delete(tempInstructorDetail);

            // commit transaction
            session.getTransaction().commit();

            System.out.println("Done!");
        }
        catch (Exception exc) {
            System.out.println("CAUGHT!");
            exc.printStackTrace();
        }
        finally {
            // handle connection leak issue
            session.close();

            factory.close();
        }
    }

}
