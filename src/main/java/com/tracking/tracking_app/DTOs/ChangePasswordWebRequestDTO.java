package com.tracking.tracking_app.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ChangePasswordWebRequestDTO {
    @NotNull
    @NotBlank(message = "Password must not be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,20}$",
            message = "Password must be at least 8 characters, contain a digit, a letter, and a special character"
    )
    private String password;

    @NotNull
    @NotBlank(message = "Confirm password must not be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,20}$",
            message = "Password must be at least 8 characters, contain a digit, a letter, and a special character"
    )
    private String confirmPassword;

    public ChangePasswordWebRequestDTO(){

    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
