package ru.hse.rekoder.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.hse.rekoder.model.User;

import java.util.Map;

@Getter
public class UserResponse extends ContentGeneratorResponse{
    @JsonProperty("id")
    private final String username;
    private final Map<String, String> contacts;

    public UserResponse(User user) {
        super(user);
        this.username = user.getUsername();
        this.contacts = user.getContacts();
    }

}
