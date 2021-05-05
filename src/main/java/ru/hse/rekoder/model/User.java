package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.validation.constraints.NotEmpty;
import java.util.*;

public class User extends ProblemOwner {
    @NotEmpty(message = "User name cannot be empty")
    private String name;

    private String bio;
    @JsonFormat(pattern="yyyy-MM-dd" /* TODO,timezone=*/)
    private Date registrationTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }
}
