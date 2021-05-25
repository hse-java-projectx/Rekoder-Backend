package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FolderRequest {
    @NotNull
    @Size(min = 1, max = 100, message = "1 <= name length <= 100")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "The folder name can only contain the following characters [a-zA-Z0-9_]")
    private String name;
}
