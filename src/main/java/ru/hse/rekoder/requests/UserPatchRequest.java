package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class UserPatchRequest {
    protected String name;
    protected String bio;
    protected Map<String, String> contacts = Collections.emptyMap();
}
