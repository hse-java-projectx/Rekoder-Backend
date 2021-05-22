package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.Team;
import ru.hse.rekoder.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserResponse extends BaseResponse {
    private final String name;
    private final String bio;
    private final Map<String, String> contacts;
    private final Date registrationDate;
    private final int rootFolderId;

    public UserResponse(User originalUser) {
        this(originalUser, "/users/" + originalUser.getId().getProblemOwnerId());
    }

    public UserResponse(User originalUser, String pathToResource) {
        super(pathToResource);
        this.name = originalUser.getName();
        this.bio = originalUser.getBio();
        this.contacts = originalUser.getContacts();
        this.registrationDate = originalUser.getRegistrationTime();
        this.rootFolderId = originalUser.getRootFolderId();
    }

    public int getRootFolderId() {
        return rootFolderId;
    }

    public Map<String, String> getContacts() {
        return contacts;
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
