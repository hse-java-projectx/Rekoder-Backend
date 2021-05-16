package ru.hse.rekoder.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.*;

public class Team extends ProblemOwner {
    @Transient
    private static final String PROBLEM_OWNER_TYPE = "team";

    private String name;
    private String bio;
    private Date registrationDate;
    private Map<String, String> contacts = Collections.emptyMap();
    @DBRef
    private List<User> members = new ArrayList<>();

    public static class TeamCompositeKey extends CompositeKey {
        public TeamCompositeKey(String name) {
            super(PROBLEM_OWNER_TYPE, name);
        }
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
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

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
