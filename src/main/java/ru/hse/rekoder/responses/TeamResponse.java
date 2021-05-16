package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamResponse extends BaseResponse {
    private final String name;
    private final String bio;
    private final Date registrationDate;
    private final Map<String, String> contacts;
    private final List<String> membersId;

    public TeamResponse(Team originalTeam, String pathToResource) {
        super(pathToResource);
        this.name = originalTeam.getName();
        this.bio = originalTeam.getBio();
        this.membersId = originalTeam.getMembers().stream().map(User::getName).collect(Collectors.toList());
        this.registrationDate = originalTeam.getRegistrationDate();
        this.contacts = originalTeam.getContacts();
    }

    public TeamResponse(Team originalTeam) {
        this(originalTeam, "/teams/" + originalTeam.getId().getProblemOwnerId());
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

    public Map<String, String> getContacts() {
        return contacts;
    }

    public List<String> getMembersId() {
        return membersId;
    }
}
