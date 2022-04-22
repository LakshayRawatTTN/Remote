package com.bootcamp.project.ecommerceapplication.domain.product;

import com.bootcamp.project.ecommerceapplication.domain.Customer;

import javax.persistence.*;

@Entity
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "customer_user_id")
    @MapsId
    private Customer customerId;
    private String review;
    private long rating;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    public ProductReview() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

}