package com.bootcamp.project.ecommerceapplication.models;

import com.bootcamp.project.ecommerceapplication.domain.category.Category;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class CategoryModel {
    @Column(unique = true)
    @NotBlank(message = "Enter Category Name")
    private String name;
    private Long parentId;

    public CategoryModel(Category category){
        this.name=category.getName();
        this.parentId=category.getParentId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}