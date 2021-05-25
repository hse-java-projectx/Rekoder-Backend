package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class TeamRequest {
    private String name;
    private String bio;
    private Map<String, String> contacts = Collections.emptyMap();
}
