package com.basemosama.studentportal.model.student;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "student_marks")
public class Marks {
    @PrimaryKey
    @ColumnInfo(name = "marks_id")
    private int id;
    @ColumnInfo(name = "marks_created_at")
    @SerializedName("created_at")
    private Date createdAt;
    @ColumnInfo(name = "marks_updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("student_id")
    private long studentId;
    @SerializedName("subject_id")
    private int subjectId;
    private double attendance;
    private double work;
    private double midterm;
    private double semester;
    @Embedded
    private Subject subject;

    public Marks(int id, Date createdAt, Date updatedAt, long studentId, int subjectId,
                 double attendance, double work, double midterm, double semester, Subject subject) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.attendance = attendance;
        this.work = work;
        this.midterm = midterm;
        this.semester = semester;
        this.subject = subject;
    }

    @Ignore
    public Marks() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getWork() {
        return work;
    }

    public void setWork(double work) {
        this.work = work;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getSemester() {
        return semester;
    }

    public void setSemester(double semester) {
        this.semester = semester;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}