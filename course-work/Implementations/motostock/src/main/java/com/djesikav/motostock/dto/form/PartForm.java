package com.djesikav.motostock.dto.form;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PartForm {

    @NotBlank(message = "Sku is required.")
    @Size(max = 40, message = "SKU cannot exceed 40 characters.")
    private String sku;
    @NotBlank(message = "Name is required.")
    @Size(max = 120, message = "Part name cannot exceed 120 characters.")
    private String name;
    @Size(max = 80, message = "Brand cannot exceed 80 characters.")
    private String brand;
    @NotNull(message = "Category is required.")
    @Positive(message = "Invalid category ID.")
    private Long categoryId;
    @NotNull(message = "Supplier is required.")
    @Positive(message = "Invalid supplier ID.")
    private Long supplierId;
    @NotNull(message = "Quantity is required.")
    @Min(value = 0, message = "Quantity cannot be negative.")
    private Integer quantity;
    @NotNull(message = "Purchase price is required.")
    @DecimalMin(value = "0.00", message = "Purchase price cannot be negative.")
    @Digits(integer = 8, fraction = 2, message = "Purchase price must have up to 8 integer digits and 2 decimal places.")
    private BigDecimal purchasePrice;
    @NotNull(message = "Selling price is required.")
    @DecimalMin(value = "0.00", message = "Selling price cannot be negative.")
    @Digits(integer = 8, fraction = 2, message = "Selling price must have up to 8 integer digits and 2 decimal places.")
    private BigDecimal sellingPrice;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
