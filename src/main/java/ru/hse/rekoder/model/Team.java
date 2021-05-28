package ru.hse.rekoder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@Document(collection = "teams")
public class Team extends ContentGenerator {
    @Indexed(unique = true)
    @JsonProperty("id")
    @NotNull(message = "Id must not be null")
    @Size(min = 1, max = 100, message = "1 <= id length <= 100")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "The id can only contain the following characters [a-zA-Z0-9_]")
    private String teamId;

    private Map<String, String> contacts = Collections.emptyMap();
    private Set<String> memberIds = new HashSet<>();
}
