package ru.hse.rekoder.controllers;

import org.springframework.stereotype.Component;
import ru.hse.rekoder.exceptions.AccessDeniedException;
import ru.hse.rekoder.model.ContentGeneratorType;
import ru.hse.rekoder.model.Owner;
import ru.hse.rekoder.services.TeamService;

@Component
public class AccessChecker {
    private final TeamService teamService;

    public AccessChecker(TeamService teamService) {
        this.teamService = teamService;
    }

    public boolean hasRequestingAccessToResourceWithOwner(Owner owner, Owner requesting) {
        switch (owner.getType()) {
            case USER:
                return requesting.getType().equals(ContentGeneratorType.USER) && owner.getId().equals(requesting.getId());
            case TEAM:
                switch (requesting.getType()) {
                    case USER:
                        return teamService.getTeam(owner.getId()).getMemberIds().contains(requesting.getId());
                    case TEAM:
                        return owner.getId().equals(requesting.getId());
                }
        }
        return false;
    }

    public void checkAccessToResourceWithOwner(Owner owner, Owner requesting) {
        if (!hasRequestingAccessToResourceWithOwner(owner, requesting)) {
            throw new AccessDeniedException();
        }
    }
}
