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
    @Transient
    public static final int MAX_DEPTH = 25;

    @NotNull(message = "Folder name must be not null")
    @Size(min = 1, max = 100, message = "1 <= name length <= 100")
    private String name;
    private Integer parentFolderId;
    private Set<@NotNull Integer> problemIds = new HashSet<>();
    private Owner owner;
}
