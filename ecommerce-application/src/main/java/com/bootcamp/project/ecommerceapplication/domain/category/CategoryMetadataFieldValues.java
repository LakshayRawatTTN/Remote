package com.bootcamp.project.ecommerceapplication.domain.category;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CategoryMetadataFieldValues")
public class CategoryMetadataFieldValues implements Serializable {

    @EmbeddedId
    private CategoryMetadataFieldValuesId categoryMetadataFieldValuesId;

    @ManyToOne
    @MapsId("category_id") //This is the name of attr in EmployerDeliveryAgentPK class
    private Category category;

    @ManyToOne
    @MapsId("category_metadata_field_id")
    private CategoryMetadataField categoryMetadataField;

    @JoinColumn(name = "value")
    private String value;


    public CategoryMetadataFieldValues() {
    }

    public CategoryMetadataFieldValuesId getCategoryMetadataFieldValuesId() {
        return categoryMetadataFieldValuesId;
    }

    public void setCategoryMetadataFieldValuesId(CategoryMetadataFieldValuesId categoryMetadataFieldValuesId) {
        this.categoryMetadataFieldValuesId = categoryMetadataFieldValuesId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryMetadataField getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(CategoryMetadataField categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}