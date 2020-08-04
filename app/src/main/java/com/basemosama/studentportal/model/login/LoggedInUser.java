package com.basemosama.studentportal.model.login;

import com.basemosama.studentportal.model.student.User;


public class LoggedInUser {
    private String api_token;
    private User student;
    private User professor;

    public LoggedInUser(String api_token, User student, User professor) {
        this.api_token = api_token;
        this.student = student;
        this.professor = professor;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
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
}

