package com.bootcamp.project.ecommerceapplication.repositories;

import com.bootcamp.project.ecommerceapplication.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


    Product findById(long id);
    @Query(value = "select * from product p where p.brand=:brand and p.category_id=:category and p.seller_id=:seller",nativeQuery = true)
    List<Product> findSameBrandCategorySeller(@Param("brand") String brand, @Param("category") long category, @Param("seller") long seller);
}
