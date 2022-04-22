package com.bootcamp.project.ecommerceapplication.domain;

import com.bootcamp.project.ecommerceapplication.domain.product.Product;
import com.bootcamp.project.ecommerceapplication.models.SellerModel;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String gst;
    private String companyContact;
    private String companyName;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "seller")
    private Set<Product> products;

    public Seller() {
    }

    public Seller (SellerModel sellerModel){
        this.gst = sellerModel.getGst();
        this.companyContact = sellerModel.getCompanyContact();
        this.companyName = sellerModel.getCompanyName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
