package com.bootcamp.project.ecommerceapplication.repositories;

import com.bootcamp.project.ecommerceapplication.domain.product.Product;
import com.bootcamp.project.ecommerceapplication.domain.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation,Long> {

    ProductVariation findById(long id);
    List<ProductVariation> findAllByProduct(Product product);

}