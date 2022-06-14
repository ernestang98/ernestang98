package com.company.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="course")
public class M_TO_M_COURSE {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    private String title;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="instructor_id")
    private M_TO_M_INSTRUCTOR instructor;

    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="course_id")
    private List<M_TO_M_REVIEW> reviews;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="course_student",
            joinColumns=@JoinColumn(name="course_id"),
            inverseJoinColumns=@JoinColumn(name="student_id")
    )
    private List<M_TO_M_STUDENT> students;


    public M_TO_M_COURSE() {

    }

    public M_TO_M_COURSE(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public M_TO_M_INSTRUCTOR getInstructor() {
        return instructor;
    }

    public void setInstructor(M_TO_M_INSTRUCTOR instructor) {
        this.instructor = instructor;
    }

    public List<M_TO_M_REVIEW> getReviews() {
        return reviews;
    }

    public void setReviews(List<M_TO_M_REVIEW> reviews) {
        this.reviews = reviews;
    }

    // add a convenience method

    public void addReview(M_TO_M_REVIEW theReview) {

        if (reviews == null) {
            reviews = new ArrayList<>();
        }

        reviews.add(theReview);
    }

    public List<M_TO_M_STUDENT> getStudents() {
        return students;
    }

    public void setStudents(List<M_TO_M_STUDENT> students) {
        this.students = students;
    }

    // add a convenience method

    public void addStudent(M_TO_M_STUDENT theStudent) {

        if (students == null) {
            students = new ArrayList<>();
        }

        students.add(theStudent);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", title=" + title + "]";
    }

}
