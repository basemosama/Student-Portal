package com.basemosama.studentportal.model.student;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "grade_table")
public class Table {
    @PrimaryKey
    @ColumnInfo(name = "table_id")
    private int id;
    @ColumnInfo(name = "grade_created_at")
    @SerializedName("created_at")
    private Date createdAt;
    @ColumnInfo(name = "grade_updated_at")
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("grade_id")
    @ColumnInfo(name = "grade_id")
    private int gradeId;

    public Table(int id, Date createdAt, Date updatedAt, String imageUrl, int gradeId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageUrl = imageUrl;
        this.gradeId = gradeId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }
}

