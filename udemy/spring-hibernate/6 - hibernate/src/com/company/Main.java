package com.company;

import com.company.controllers.*;
import com.company.models.Instructor;
import com.company.models.InstructorDetail;
import com.company.models.Student;
import com.company.models.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

/*
    // SECTION 19
    public static void main(String[] args) {
        String jdbc_url = "jdbc:mysql://localhost:3306/" + System.getenv("dbname") + "?useSSL=false&serverTimezone=UTC";
        String user = System.getenv("user");
        String pass = System.getenv("pass");

	    try {
            System.out.println("connecting to database: " + jdbc_url);
            Connection conn = DriverManager.getConnection(jdbc_url, user, pass);
            System.out.println("success!");
        }
	    catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Error encountered");
	        System.err.println(e);
        }
    }
 */

/*
    // Student & StudentController
    // SECTION 20 - 21 (Hibernate Config + basics)
    // Read index n of student table, create 2 records in student table, query the entire student table
    // update index n - 1 in student table, delete index n - 2 in student table
    public static void main(String[] args) {
        int idOfStudentToBeRead = 6;
        int idOfStudentToBeUpdated = idOfStudentToBeRead - 1;
        int idOfStudentToBeDeleted = idOfStudentToBeRead - 2;
        StudentController.readStudent(idOfStudentToBeRead);
        StudentController.createStudent("alvin", "doe", "alvindoe@yahoo.com");
        StudentController.createStudent("ernest", "ang", "ernestang@gmail.com");
        StudentController.queryStudent();
        StudentController.updateStudents(idOfStudentToBeUpdated);
        StudentController.deleteStudent(idOfStudentToBeDeleted);
    }
 */

/*
    // Instructor, InstructorDetail & InstructorController, InstructorDetailController
    // SECTION 22 - 23
    // Creating instructor and its details
    // Deleting instructor or the details will delete the other vice versa
    // If you want to only delete the instructor details or instructor, then you must break the bidirectional link
    // You can't break the bidrectional link from the instructor side (i.e. delete instructor without deleting the
    // instructor details, below error observed.
    // link: https://stackoverflow.com/questions/2302802/how-to-fix-the-hibernate-object-references-an-unsaved-transient-instance-save)
    public static void main(String[] args) {
        int idOfInstructorToBeDeletedWithDetail = 5;
        InstructorController.createInstructor(
                "Madhu",
                "Patel",
                "madhu@luv2code.com",
                "http://www.youtube.com",
                "Guitar");
        InstructorDetailController.DeleteInstructorDetail(idOfInstructorToBeDeletedWithDetail);
    }
 */

/*
    // Course, Instructor_1, InstructorDetail_1 & Instructor1Controller
    // SECTION 24
    // STOPPED HERE
    public static void main(String[] args) {
        InstructorWithInstructorDetailWithCourseController.createInstructor1(
                "Ernest",
                "ang",
                "ernest@asd.com",
                "youtube blahblahblah",
                "cycling"
        );
        InstructorWithInstructorDetailWithCourseController.createCourses();
        InstructorWithInstructorDetailWithCourseController.getInstructor1AndCourse();
        InstructorWithInstructorDetailWithCourseController.getInstructor1AndInstructorDetail1();
    }
 */
    
/*
    // Eager Loading - Retrieve everything
    // With Eager Loading, instructor and all of the courses is loaded at once
    // Lazy Loading - Retrieve on request (BEST PRACTICE)
    // Use case: Use lazy loading for search results, only load instructors... not their courses (see image)
    // Use case: Use eager loading to retrieve instructor and all of their courses (see image)
    // Can view Default Fetch Types for the different mappings
    // You can specify the fetch type to override the original fetch type
    // https://www.tutorialspoint.com/difference-between-lazy-and-eager-loading-in-hibernate
    // Course_Eager_Loading, Instructor_1_Eager_Loading, InstructorDetail_1_Eager_Loading & InstructorEagerController
    // SECTION 25
    public static void main(String[] args) {
        InstructorEagerController.easyLazyDemo();
        InstructorEagerController.fetchJoinDemo();
        InstructorEagerController.getCoursesLater();
    }
*/

/*
    // Course_Eager_Loading, Instructor_1_Eager_Loading, InstructorDetail_1_Eager_Loading & InstructorEagerController
    // SECTION 26
    public static void main(String[] args) {
        CourseReviewController.createCourseAndReviews();
        CourseReviewController.getCourseAndReviews(14);
        CourseReviewController.deleteCourseAndReviews(14);
    }
 */

/*
    // M_TO_M things, dbname is also different - spring-m-2-m (make sure to change and change back)
    // SECTION 27
    public static void main(String[] args) {
        MToMController.createCourseAndStudent();
        MToMController.getCourses();
        MToMController.deleteCourse();
        MToMController.deleteStudent();
        MToMController.createCourseAndReview();
        MToMController.createStudentsOnly();
        MToMController.addCoursesToStudent();
    }
 */

}
