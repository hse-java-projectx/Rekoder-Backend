package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class UserPatchRequest {
    protected String name;
    protected String bio;
    @NotNull(message = "The contacts must not be null if they specify in request body")
    protected Map<String, String> contacts = Collections.emptyMap();
}
