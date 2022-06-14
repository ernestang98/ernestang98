package com.company.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="instructor_1")
public class Instructor_1_Eager_Loading {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="instructor_detail_1_id")
    private InstructorDetail_1_Eager_Loading instructorDetail;

    // Line 27 in Course.java
//    @OneToMany(mappedBy="instructor123",
    @OneToMany(mappedBy="instructorTEST",
            fetch=FetchType.LAZY,
//            fetch=FetchType.EAGER,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<Course_Eager_Loading> courses;


    public Instructor_1_Eager_Loading() {

    }

    public Instructor_1_Eager_Loading(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InstructorDetail_1_Eager_Loading getInstructorDetail() {
        return instructorDetail;
    }

    public void setInstructorDetail(InstructorDetail_1_Eager_Loading instructorDetail) {
        this.instructorDetail = instructorDetail;
    }

    @Override
    public String toString() {
        return "Instructor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", instructorDetail=" + instructorDetail + "]";
    }

    public List<Course_Eager_Loading> getCourses() {
        return courses;
    }

    public void setCourses(List<Course_Eager_Loading> courses) {
        this.courses = courses;
    }

    // add convenience methods for bi-directional relationship

    public void add(Course_Eager_Loading tempCourse) {

        if (courses == null) {
            courses = new ArrayList<>();
        }

        courses.add(tempCourse);

        tempCourse.setInstructor(this);
    }

}











