package com.company.models;

import javax.persistence.*;

@Entity
@Table(name="instructor_detail")
public class M_TO_M_INSTRUCTORDETAIL {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="youtube_channel")
    private String youtubeChannel;

    @Column(name="hobby")
    private String hobby;

    // add new field for instructor (also add getter/setters)

    // add @OneToOne annotation

    @OneToOne(mappedBy="instructorDetail",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    private M_TO_M_INSTRUCTOR instructor;


    public M_TO_M_INSTRUCTOR getInstructor() {
        return instructor;
    }

    public void setInstructor(M_TO_M_INSTRUCTOR instructor) {
        this.instructor = instructor;
    }

    public M_TO_M_INSTRUCTORDETAIL() {

    }

    public M_TO_M_INSTRUCTORDETAIL(String youtubeChannel, String hobby) {
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
