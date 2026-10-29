package com.djesikav.motostock.dto.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryForm {

    @NotBlank(message = "Category name is required.")
    @Size(max = 80, message = "Category name cannot exceed 80 characters.")
    private String name;
    @Size(max = 255, message = "Description cannot exceed 255 characters.")
    private String description;
    @NotNull(message = "Display order is required.")
    @Min(value = 0, message = "Display order cannot be negative.")
    private Integer displayOrder = 0;
    @NotNull(message = "Active status is required.")
    private Boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
