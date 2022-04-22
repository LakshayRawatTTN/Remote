package com.bootcamp.project.ecommerceapplication.models.product;

import com.bootcamp.project.ecommerceapplication.domain.category.Category;
import com.bootcamp.project.ecommerceapplication.domain.product.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ProductModel {

    @Positive(message = "enter category Id")
    private long categoryId;
    @NotBlank(message = "enter product name")
    private String name;
    @NotBlank(message = "enter brand name")
    private String brand;
    private String description;
    private boolean is_returnable;
    private boolean is_cancellable;

    public ProductModel() {

    }
    public ProductModel(Product product) {

        this.brand = product.getBrand();
        this.name = product.getName();
        this.description = product.getDescription();
        this.categoryId = product.getCategoryId().getId();
        this.is_returnable = product.isIs_returnable();
        this.is_cancellable = product.isIs_cancellable();

    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_returnable() {
        return is_returnable;
    }

    public void setIs_returnable(boolean is_returnable) {
        this.is_returnable = is_returnable;
    }

    public boolean isIs_cancellable() {
        return is_cancellable;
    }

    public void setIs_cancellable(boolean is_cancellable) {
        this.is_cancellable = is_cancellable;
    }
}
