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
    private String name;
}
