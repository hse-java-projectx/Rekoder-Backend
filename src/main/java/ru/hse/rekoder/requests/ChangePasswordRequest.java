package ru.hse.rekoder.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotNull(message = "The old password must not be null")
    private String oldPassword;
    @NotNull(message = "The new password must not be null")
    @Size(min = 5, message = "The new password length must be greater than 5")
    private String newPassword;
}
