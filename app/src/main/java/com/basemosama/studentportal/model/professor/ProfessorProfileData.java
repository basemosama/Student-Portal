package com.basemosama.studentportal.model.professor;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.basemosama.studentportal.database.typeconverters.OfficeHourListConverter;
import com.basemosama.studentportal.database.typeconverters.ProjectListConverter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "professor_profile")
public class ProfessorProfileData {
    @PrimaryKey
    private int id;
    private String name;
    private String email;
    @SerializedName("national_id")
    private String nationalId;
    private String address;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("faculty_id")
    private int facultyId;
    @SerializedName("department_id")
    private int departmentId;
    @SerializedName("group_id")
    private int groupId;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @Embedded(prefix = "grade_")
    private ProfessorGrade department;
    @SerializedName("api_token")
    private String apiToken;
    @SerializedName("offices")
    @TypeConverters(OfficeHourListConverter.class)
    private List<OfficeHour> officeHours;
    @TypeConverters(ProjectListConverter.class)
    private List<ProfessorProject> projects;
    private long timeStamp;

    public ProfessorProfileData(int id, String name, String email, String nationalId, String address, String imageUrl, int facultyId, int departmentId, int groupId,
                                Date createdAt, Date updatedAt, ProfessorGrade department, String apiToken, List<OfficeHour> officeHours, List<ProfessorProject> projects, long timeStamp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nationalId = nationalId;
        this.address = address;
        this.imageUrl = imageUrl;
        this.facultyId = facultyId;
        this.departmentId = departmentId;
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.department = department;
        this.apiToken = apiToken;
        this.officeHours = officeHours;
        this.projects = projects;
        this.timeStamp = timeStamp;
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

    public ProfessorGrade getDepartment() {
        return department;
    }

    public void setDepartment(ProfessorGrade department) {
        this.department = department;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public List<OfficeHour> getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(List<OfficeHour> officeHours) {
        this.officeHours = officeHours;
    }

    public List<ProfessorProject> getProjects() {
        return projects;
    }

    public void setProjects(List<ProfessorProject> projects) {
        this.projects = projects;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
