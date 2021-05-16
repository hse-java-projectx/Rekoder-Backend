package ru.hse.rekoder.responses;

import ru.hse.rekoder.model.User;

import java.util.Date;

public class UserResponse extends BaseResponse {
    private final String name;
    private final String bio;
    private final Date registrationDate;

    public UserResponse(User originalUser) {
        this(originalUser, "/users/" + originalUser.getId().getProblemOwnerId());
    }

    public UserResponse(User originalUser, String pathToResource) {
        super(pathToResource);
        this.name = originalUser.getName();
        this.bio = originalUser.getBio();
        this.registrationDate = originalUser.getRegistrationTime();
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
