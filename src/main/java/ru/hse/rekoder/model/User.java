package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotEmpty;
import java.util.*;

public class User extends ProblemOwner {
    @Transient
    public static final String PROBLEM_OWNER_TYPE = "user";

    public static class UserCompositeKey extends CompositeKey {
        public UserCompositeKey(String name) {
            super(PROBLEM_OWNER_TYPE, name);
        }

        public UserCompositeKey(String problemOwnerType, String name) {
            super(problemOwnerType, name);
        }

        public UserCompositeKey() {
        }
    }

    @NotEmpty(message = "User name cannot be empty")
    private String username;
    private String password;

    private String name;

    private String bio;
    @JsonFormat(pattern="yyyy-MM-dd" /* TODO,timezone=*/)
    private Date registrationTime;
    private Map<String, String> contacts = Collections.emptyMap();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
