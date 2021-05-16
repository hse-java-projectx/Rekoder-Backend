package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class TeamResponse extends BaseResponse {
    private final String name;
    private final String bio;
    private final List<String> membersId;

    public TeamResponse(Team originalTeam, String pathToResource) {
        super(pathToResource);
        this.name = originalTeam.getName();
        this.bio = originalTeam.getBio();
        this.membersId = originalTeam.getMembers().stream().map(User::getName).collect(Collectors.toList());
    }

    public TeamResponse(Team originalTeam) {
        this(originalTeam, "/teams/" + originalTeam.getId().getProblemOwnerId());
    }
}
