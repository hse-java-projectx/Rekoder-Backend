package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@Document(collection = "users")
public class User extends ContentGenerator {
    @NotNull
    @Size(min = 1, max = 100, message = "1 <= username length <= 100")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "The username can only contain the following characters [a-zA-Z0-9_]")
    private String username;
    @NotNull
    @Size(min = 1)
    private String password;

    private Map<String, String> contacts = Collections.emptyMap();
}
