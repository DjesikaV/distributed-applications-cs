package com.djesikav.motostock.dto.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SupplierForm {

    @NotBlank(message = "Supplier name is required.")
    @Size(max = 100, message = "Supplier name cannot exceed 100 characters.")
    String name;
    @Email(message = "Invalid email")
    @Size(max = 120, message = "Email cannot exceed 120 characters.")
    String email;
    @NotNull(message = "Priority is required.")
    Integer priority;
    @NotNull(message = "Active status is required.")
    Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
