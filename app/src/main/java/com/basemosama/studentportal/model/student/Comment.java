package com.basemosama.studentportal.model.student;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "student_comments")
public class Comment {
    @PrimaryKey
    private long id;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("post_id")
    private long postId;
    private String body;
    private String email;
    @SerializedName("student_id")
    private int studentId;
    @Embedded(prefix = "student_")
    private User student;
    @Embedded(prefix = "professor_")
    private User professor;
    @Embedded(prefix = "replay_")
    private Reply latestReply;
    private long noOfReplies;
    private long noOfLikes;
    @Ignore
    private List<Reply> replies;


    public Comment(long id, Date createdAt, Date updatedAt, long postId, String body, String email,
                   int studentId, User student, User professor, Reply latestReply, long noOfReplies, long noOfLikes) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postId = postId;
        this.body = body;
        this.email = email;
        this.studentId = studentId;
        this.student = student;
        this.professor = professor;
        this.latestReply = latestReply;
        this.noOfReplies = noOfReplies;
        this.noOfLikes = noOfLikes;
    }

    @Ignore
    public Comment(long id, Date createdAt, Date updatedAt, long postId, String body, String email, int studentId,
                   User student, User professor, Reply latestReply, long noOfReplies, long noOfLikes, List<Reply> replies) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postId = postId;
        this.body = body;
        this.email = email;
        this.studentId = studentId;
        this.student = student;
        this.professor = professor;
        this.latestReply = latestReply;
        this.noOfReplies = noOfReplies;
        this.noOfLikes = noOfLikes;
        this.replies = replies;
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

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public Reply getLatestReply() {
        return latestReply;
    }

    public void setLatestReply(Reply latestReply) {
        this.latestReply = latestReply;
    }

    public long getNoOfReplies() {
        return noOfReplies;
    }

    public void setNoOfReplies(long noOfReplies) {
        this.noOfReplies = noOfReplies;
    }

    public long getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(long noOfLikes) {
        this.noOfLikes = noOfLikes;
    }
}


