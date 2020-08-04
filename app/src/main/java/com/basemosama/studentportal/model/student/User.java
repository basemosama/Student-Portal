package com.basemosama.studentportal.model.student;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "students")
public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @PrimaryKey
    private int id;
    private String name;
    private String email;
    @SerializedName("national_id")
    private String nationalId;
    private String address;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("score")
    private double gpa;
    @SerializedName("faculty_id")
    private int facultyId;
    @SerializedName("department_id")
    private int departmentId;
    @SerializedName("grade_id")
    private int gradeId;
    @SerializedName("group_id")
    private int groupId;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("api_token")
    private String apiToken;
    private long timeStamp;

    public User(int id, String name, String email, String nationalId, String address, String imageUrl, double gpa,
                int facultyId, int departmentId, int gradeId, int groupId, Date createdAt, Date updatedAt, String apiToken, long timeStamp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nationalId = nationalId;
        this.address = address;
        this.imageUrl = imageUrl;
        this.gpa = gpa;
        this.facultyId = facultyId;
        this.departmentId = departmentId;
        this.gradeId = gradeId;
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.apiToken = apiToken;
        this.timeStamp = timeStamp;
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        nationalId = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        gpa = in.readDouble();
        facultyId = in.readInt();
        departmentId = in.readInt();
        gradeId = in.readInt();
        groupId = in.readInt();
        apiToken = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(nationalId);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeDouble(gpa);
        dest.writeInt(facultyId);
        dest.writeInt(departmentId);
        dest.writeInt(gradeId);
        dest.writeInt(groupId);
        dest.writeString(apiToken);
    }
}

