package com.company.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="course")
public class Course_Eager_Loading {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title")
    private String title;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="instructor_1_id")
    private Instructor_1_Eager_Loading instructorTEST;

    // Course & Reviews
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="course_id")
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }

        reviews.add(review);
    }

    public Course_Eager_Loading() {

    }

    public Course_Eager_Loading(String title) {
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

    public Instructor_1_Eager_Loading getInstructor() {
        return instructorTEST;
    }

    public void setInstructor(Instructor_1_Eager_Loading instructor) {
        this.instructorTEST = instructor;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", title=" + title + "]";
    }


}
