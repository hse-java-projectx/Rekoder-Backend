package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserResponse extends BaseResponse {
    private final String name;
    private final String bio;
    private final Map<String, String> contacts;
    private final Date registrationDate;
    private final List<String> teamIds;

    public UserResponse(User originalUser) {
        this(originalUser, "/users/" + originalUser.getId().getProblemOwnerId());
    }

    public UserResponse(User originalUser, String pathToResource) {
        super(pathToResource);
        this.name = originalUser.getName();
        this.bio = originalUser.getBio();
        this.contacts = originalUser.getContacts();
        this.registrationDate = originalUser.getRegistrationTime();
        this.teamIds = originalUser.getTeams().stream().map(Team::getName).collect(Collectors.toList());
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public List<String> getTeamIds() {
        return teamIds;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }
}
