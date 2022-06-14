package com.company.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="course")
public class Course {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    private String title;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="instructor_1_id")
    private Instructor_1 instructorTEST;

    public Course() {

    }

    public Course(String title) {
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

    public Instructor_1 getInstructor() {
        return instructorTEST;
    }

    public void setInstructor(Instructor_1 instructor) {
        this.instructorTEST = instructor;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", title=" + title + "]";
    }


}
