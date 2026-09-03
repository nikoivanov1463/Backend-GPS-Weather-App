package com.tracking.tracking_app.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public class UserRequestDTO {
    @NotNull
    @NotBlank(message = "Name must not be empty")
    @Size(min = 4, max = 20, message = "Invalid size (between 4 and 20)")
    private final String username;
    @NotNull
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private final String email;
    @NotNull
    @NotBlank(message = "Password must not be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,20}$", message =
            "Must contain at least one letter, one digit, one special character and its length (8 - 20)")
    private final String password;

    @JsonCreator
    public UserRequestDTO(@JsonProperty("username") String username, @JsonProperty("email") String email, @JsonProperty("password") String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
