package ru.hse.rekoder.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.hse.rekoder.model.Team;

import java.util.*;

@Getter
public class TeamResponse extends ContentGeneratorResponse {
    @JsonProperty("id")
    private final String teamId;
    private final Map<String, String> contacts;
    private final Set<String> memberIds;

    public TeamResponse(Team team) {
        super(team);
        this.teamId = team.getTeamId();
        this.contacts = team.getContacts();
        this.memberIds = team.getMemberIds();
    }
}
