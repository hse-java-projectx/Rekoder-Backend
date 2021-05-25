package ru.hse.rekoder.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "folders")
public class Folder extends DocumentWithIncreasingIdSequence {
    @NotNull(message = "Folder name must be not null")
    @Size(min = 1, max = 100, message = "1 <= name length <= 100")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "The folder name can only contain the following characters [a-zA-Z0-9_]")
    private String name;
    private Integer parentFolderId;
    private Set<@NotNull Integer> problemIds = new HashSet<>();
    private Owner owner;
}
