package com.bootcamp.project.ecommerceapplication.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Set;

public class CategoryMetadataValueModel {

    @Positive(message = "enter category id")
    private long categoryId;
    @Positive(message = "enter metadata id")
    private long metadataId;
    @NotBlank(message = "enter values")
    private String values;

    public CategoryMetadataValueModel() {
    }

    public CategoryMetadataValueModel(long categoryId, long metadataId, String values) {
        this.categoryId = categoryId;
        this.metadataId = metadataId;
        this.values = values;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(long metadataId) {
        this.metadataId = metadataId;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
