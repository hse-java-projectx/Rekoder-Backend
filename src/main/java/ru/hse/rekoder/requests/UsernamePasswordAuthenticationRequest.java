package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UsernamePasswordAuthenticationRequest {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
