package ru.hse.rekoder.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotEmpty;
import java.util.*;

public class Team extends ProblemOwner {
    @Transient
    private static final String PROBLEM_OWNER_TYPE = "team";

    @NotEmpty(message = "Team name cannot be empty")
    private String name;
    private String bio;
    private Date registrationDate;
    private Map<String, String> contacts = Collections.emptyMap();
    private Set<String> memberIds = new HashSet<>();

    public static class TeamCompositeKey extends CompositeKey {
        public TeamCompositeKey(String name) {
            super(PROBLEM_OWNER_TYPE, name);
        }

        public TeamCompositeKey(String problemOwnerType, String name) {
            super(problemOwnerType, name);
        }

        public TeamCompositeKey() {
        }
    }

    public Set<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<String> memberIds) {
        this.memberIds = memberIds;
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
}
