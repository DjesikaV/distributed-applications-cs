package com.djesikav.motostock.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

    @NotBlank(message = "Username is required.")
    @Size(max = 50, min = 3, message = "Username must be between 3 and 50 characters.")
    private String username;
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required.")
    @Size(max = 120, message = "Email cannot exceed 120 characters.")
    private String email;
    @NotBlank(message = "Password is required.")
    @Size(max = 72, min = 8, message = "Password must be between 8 and 72 characters.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
