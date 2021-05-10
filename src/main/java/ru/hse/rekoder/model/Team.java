package ru.hse.rekoder.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

public class Team extends ProblemOwner {
    @Transient
    private static final String PROBLEM_OWNER_TYPE = "team";

    private String name;
    private String bio;
    @DBRef
    private List<User> members = new ArrayList<>();

    public static class TeamCompositeKey extends CompositeKey {
        public TeamCompositeKey(String name) {
            super(PROBLEM_OWNER_TYPE, name);
        }
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
