package com.basemosama.studentportal.model.student;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "sources")
public class Source {
    @PrimaryKey
    @ColumnInfo(name = "source_id")
    private int id;
    private String title;
    private String path;
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private Date createdAt;
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("professor_id")
    private int professorId;
    @SerializedName("grade_id")
    private int gradeId;
    @SerializedName("subject_id")
    private int subjectId;
    @Embedded(prefix = "prof_")
    private User professor;
    @Embedded(prefix = "subject_")
    private Subject subject;
    @Embedded(prefix = "grade_")
    private ProfessorGrade grade;

    public Source(int id, String title, String path, Date createdAt, Date updatedAt, int professorId,
                  int gradeId, int subjectId, User professor, Subject subject, ProfessorGrade grade) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.professorId = professorId;
        this.gradeId = gradeId;
        this.subjectId = subjectId;
        this.professor = professor;
        this.subject = subject;
        this.grade = grade;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ProfessorGrade getGrade() {
        return grade;
    }

    public void setGrade(ProfessorGrade grade) {
        this.grade = grade;
    }

}
