package com.bootcamp.project.ecommerceapplication.repositories;

import com.bootcamp.project.ecommerceapplication.domain.category.CategoryMetadataField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetadataField,Long> {
    CategoryMetadataField findByName(String name);
    List<CategoryMetadataField> findAll();
    CategoryMetadataField findById(long id);
//

}