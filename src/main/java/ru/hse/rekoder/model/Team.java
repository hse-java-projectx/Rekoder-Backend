package ru.hse.rekoder.model;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class Team extends ProblemOwner {
    private String bio;
    @NotEmpty(message = "Team must consist of at least one user")
    private List<User> members;

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
