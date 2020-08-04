package com.basemosama.studentportal.model.student;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "student_posts")
public class Post {
    @PrimaryKey
    private long id;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("student_id")
    private long studentId;
    @SerializedName("professor_id")
    private long professorId;
    private String title;
    private String body;
    @SerializedName("group_id")
    private int groupId;
    @Embedded(prefix = "student_")
    private User student;
    @Embedded(prefix = "professor_")
    private User professor;
    private long noOfComments;
    private long noOfLikes;
    @Ignore
    private List<Comment> comments;

    public Post(long id, Date createdAt, Date updatedAt, long studentId, long professorId, String title,
                String body, int groupId, User student, User professor, long noOfComments, long noOfLikes) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.studentId = studentId;
        this.professorId = professorId;
        this.title = title;
        this.body = body;
        this.groupId = groupId;
        this.student = student;
        this.professor = professor;
        this.noOfComments = noOfComments;
        this.noOfLikes = noOfLikes;
    }

    @Ignore
    public Post(long id, Date createdAt, Date updatedAt, long studentId, long professorId, String title,
                String body, int groupId, User student, User professor, long noOfComments, long noOfLikes, List<Comment> comments) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.studentId = studentId;
        this.professorId = professorId;
        this.title = title;
        this.body = body;
        this.groupId = groupId;
        this.student = student;
        this.professor = professor;
        this.noOfComments = noOfComments;
        this.noOfLikes = noOfLikes;
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(long professorId) {
        this.professorId = professorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(long noOfComments) {
        this.noOfComments = noOfComments;
    }

    public long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(long noOfLikes) {
        this.noOfLikes = noOfLikes;
    }
}
