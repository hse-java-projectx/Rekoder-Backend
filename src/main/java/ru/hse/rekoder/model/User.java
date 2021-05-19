package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotEmpty;
import java.util.*;

public class User extends ProblemOwner {
    @Transient
    public static final String PROBLEM_OWNER_TYPE = "user";

    public static class UserCompositeKey extends CompositeKey {
        public UserCompositeKey(String name) {
            super(PROBLEM_OWNER_TYPE, name);
        }
    }

    @NotEmpty(message = "User name cannot be empty")
    private String name;

    private String bio;
    @JsonFormat(pattern="yyyy-MM-dd" /* TODO,timezone=*/)
    private Date registrationTime;
    @DBRef(lazy = true)
    private List<Team> teams = new ArrayList<>();
    private Map<String, String> contacts = Collections.emptyMap();

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

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
