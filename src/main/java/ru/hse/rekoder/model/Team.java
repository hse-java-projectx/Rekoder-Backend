package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@Document(collection = "teams")
public class Team extends ContentGenerator {
    private String teamId;

    private Map<String, String> contacts = Collections.emptyMap();
    private Set<String> memberIds = new HashSet<>();
}
