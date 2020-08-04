package com.basemosama.studentportal.model.student;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.basemosama.studentportal.database.typeconverters.DateConverter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey
    private int id;
    private String title;
    @SerializedName("body")
    private String description;
    private String owner;
    @SerializedName("department_id")
    private int departmentId;
    @SerializedName("day")
    @TypeConverters(DateConverter.class)
    private Date date;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("created_at")
    @TypeConverters(DateConverter.class)
    private Date createdAt;
    @SerializedName("updated_at")
    @TypeConverters(DateConverter.class)
    private Date updatedAt;


    public Event(int id, String title, String description, String owner, int departmentId, Date date, String imageUrl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.departmentId = departmentId;
        this.date = date;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

}
