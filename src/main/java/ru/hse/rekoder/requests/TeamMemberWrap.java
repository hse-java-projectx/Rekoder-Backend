package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TeamMemberWrap {
    @NotNull(message = "memberId must not be null")
    @Size(min = 1, max = 100, message = "1 <= memberId length <= 100")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "The memberId can only contain the following characters [a-zA-Z0-9_]")
    private String memberId;
}
