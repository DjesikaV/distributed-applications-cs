package com.djesikav.motostock.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "parts")
public class Part extends BaseEntity {

    @Column(name = "sku", nullable = false, unique = true, length = 40)
    private String sku;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "brand", length = 80)
    private String brand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceFormat;

    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPriceFormat;

    public BigDecimal getSellingPriceFormat() {
        return sellingPriceFormat;
    }

    public void setSellingPriceFormat(BigDecimal sellingPriceFormat) {
        this.sellingPriceFormat = sellingPriceFormat;
    }

    public BigDecimal getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(BigDecimal priceFormat) {
        this.priceFormat = priceFormat;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
