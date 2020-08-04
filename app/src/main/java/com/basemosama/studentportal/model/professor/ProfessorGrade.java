package com.basemosama.studentportal.model.professor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity(tableName = "professor_grades")
public class ProfessorGrade {
    @PrimaryKey
    @ColumnInfo(name = "prof_grade_id")
    private int id;
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private Date createdAt;
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;
    private String name;
    private long timeStamp;

    public ProfessorGrade(int id, Date createdAt, Date updatedAt, String name, long timeStamp) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.timeStamp = timeStamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}