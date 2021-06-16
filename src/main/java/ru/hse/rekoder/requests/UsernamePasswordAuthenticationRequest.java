package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UsernamePasswordAuthenticationRequest {
    @NotNull(message = "The id must not be null")
    private String id;
    @NotNull(message = "The password must not be null")
    private String password;
}
