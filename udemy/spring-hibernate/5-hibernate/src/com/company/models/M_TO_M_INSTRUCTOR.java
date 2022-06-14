package com.company.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="instructor")
public class M_TO_M_INSTRUCTOR {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="instructor_detail_id")
    private M_TO_M_INSTRUCTORDETAIL instructorDetail;

    @OneToMany(fetch=FetchType.LAZY,
            mappedBy="instructor",
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<M_TO_M_COURSE> courses;


    public M_TO_M_INSTRUCTOR() {

    }

    public M_TO_M_INSTRUCTOR(String firstName, String lastName, String email) {
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

    public M_TO_M_INSTRUCTORDETAIL getInstructorDetail() {
        return instructorDetail;
    }

    public void setInstructorDetail(M_TO_M_INSTRUCTORDETAIL instructorDetail) {
        this.instructorDetail = instructorDetail;
    }

    @Override
    public String toString() {
        return "Instructor [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
                + ", instructorDetail=" + instructorDetail + "]";
    }

    public List<M_TO_M_COURSE> getCourses() {
        return courses;
    }

    public void setCourses(List<M_TO_M_COURSE> courses) {
        this.courses = courses;
    }

    // add convenience methods for bi-directional relationship

    public void add(M_TO_M_COURSE tempCourse) {

        if (courses == null) {
            courses = new ArrayList<>();
        }

        courses.add(tempCourse);

        tempCourse.setInstructor(this);
    }
}
