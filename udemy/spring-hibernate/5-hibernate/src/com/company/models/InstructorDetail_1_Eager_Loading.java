package com.company.models;

import javax.persistence.*;

@Entity
@Table(name="instructor_detail_1")
public class InstructorDetail_1_Eager_Loading {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="youtube_channel")
    private String youtubeChannel;

    @Column(name="hobby")
    private String hobby;

    @OneToOne(mappedBy="instructorDetail",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    private Instructor_1_Eager_Loading instructor;


    public Instructor_1_Eager_Loading getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor_1_Eager_Loading instructor) {
        this.instructor = instructor;
    }

    public InstructorDetail_1_Eager_Loading() {

    }

    public InstructorDetail_1_Eager_Loading(String youtubeChannel, String hobby) {
        this.youtubeChannel = youtubeChannel;
        this.hobby = hobby;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "InstructorDetail [id=" + id + ", youtubeChannel=" + youtubeChannel + ", hobby=" + hobby + "]";
    }

}
