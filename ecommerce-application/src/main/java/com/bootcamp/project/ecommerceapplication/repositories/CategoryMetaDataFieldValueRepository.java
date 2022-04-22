package com.bootcamp.project.ecommerceapplication.repositories;

import com.bootcamp.project.ecommerceapplication.domain.category.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryMetaDataFieldValueRepository extends JpaRepository<CategoryMetadataFieldValues, Long> {

    @Modifying
    @Query(value = "insert into category_metadata_field_values values(:id,:mid,:value)", nativeQuery = true)
    void addMetadataValues(@Param("id") long id, @Param("mid") long mid, @Param("value") String value);

    @Query(value = "select * from category_metadata_field_values  where category_id=:id and category_metadata_field_id=:mid", nativeQuery = true)
    CategoryMetadataFieldValues viewMetadataValues(@Param("id") long id, @Param("mid") long mid);

    @Query(value = "select * from category_metadata_field_values  where category_id=:id and category_metadata_field_id=:mid", nativeQuery = true)
    List<Object[]> viewMetadataValues2(@Param("id") long id, @Param("mid") long mid);


    @Query(value = "select * from category_metadata_field_values", nativeQuery = true)
    List<Object> viewAllMetadataValue();


    @Modifying
    @Query(value = "update category_metadata_field_values set value=:value where category_id=:id and category_metadata_field_id=:mid", nativeQuery = true)
    void updateMetadataValues(@Param("id") long id, @Param("mid") long mid, @Param("value") String value);
}