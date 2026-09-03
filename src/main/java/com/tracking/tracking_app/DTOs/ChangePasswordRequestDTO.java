package com.tracking.tracking_app.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChangePasswordRequestDTO {
    @NotNull
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private final String email;

    ChangePasswordRequestDTO(@JsonProperty("email") String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
