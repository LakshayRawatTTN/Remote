package com.bootcamp.project.ecommerceapplication.models.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Map;

public class ProductVariationModel {
    @Positive(message = "Enter Product Id")
    private long productId;
    @NotNull(message = "Enter metadata")
    private Map<String,String> metadata;
    @Positive( message = "Enter quantitiy > 0")
    private long quantity;
    @Positive(message = "Enter price > 0")
    private long price;


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}