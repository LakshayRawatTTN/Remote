package com.bootcamp.project.ecommerceapplication.services;

import com.bootcamp.project.ecommerceapplication.domain.Seller;
import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.domain.category.Category;
import com.bootcamp.project.ecommerceapplication.domain.category.CategoryMetadataField;
import com.bootcamp.project.ecommerceapplication.domain.product.Product;
import com.bootcamp.project.ecommerceapplication.domain.product.ProductVariation;
import com.bootcamp.project.ecommerceapplication.exceptions.FieldExist;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.product.ProductModel;
import com.bootcamp.project.ecommerceapplication.models.product.ProductVariationModel;
import com.bootcamp.project.ecommerceapplication.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    private CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    public ResponseEntity<ProductModel> addProduct(ProductModel productModel) throws FieldExist, UserNotFoundException {
        User user = User.currentUser();

        if (user == null) {
            throw new UserNotFoundException("User not found please login");
        }
        Seller seller = sellerRepository.findById(user.getId());
        System.out.println(seller);
        Category category = categoryRepository.findById(productModel.getCategoryId());
        if (category == null) {
            throw new FieldExist("category does not exist for the product");
        }
        if (category.getParentId() == 0) {
            throw new FieldExist("You can't add a product to a parent Category");
        }

        List<Product> products = productRepository.findSameBrandCategorySeller(productModel.getBrand(), productModel.getCategoryId(), seller.getId());

        if (products != null)
            for (Product product : products) {
                if (product.getName().equals(productModel.getName())) {
                    throw new FieldExist("A product with same brand and category already exist");
                }
            }
        Product product = new Product();
        product.setName(productModel.getName());
        product.setBrand(productModel.getBrand());
        product.setCategoryId(category);
        product.setSeller(seller);
        product.setDescription(productModel.getDescription());
        product.setIs_cancellable(productModel.isIs_cancellable());
        product.setIs_returnable(productModel.isIs_cancellable());
        productRepository.save(product);

        return new ResponseEntity<>(productModel, HttpStatus.CREATED);

    }

    public ResponseEntity<ProductVariationModel> addProductVariation(ProductVariationModel productVariationModel) throws FieldExist {

        Product product = productRepository.findById(productVariationModel.getProductId());
        System.out.println(product);
        if (product == null) {
            throw new FieldExist("Product doesn't exist");
        }
        Map<String, String> metadataExample = productVariationModel.getMetadata();
        for (Map.Entry<String, String> entry : metadataExample.entrySet()) {
            CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findByName(entry.getKey());
            if (categoryMetadataField == null) {
                throw new FieldExist("Category Metadata Field doesn't Exist");
            }
            List<Object[]> result = categoryMetaDataFieldValueRepository.viewMetadataValues2(product.getCategoryId().getId(), categoryMetadataField.getId());
            if (result.isEmpty()) {
                throw new FieldExist("No Metadata values Exist for category id:-" + product.getCategoryId().getId() + " and field id:-" + categoryMetadataField.getId());
            }
            Object[] temp = result.get(0);
            String[] str = temp[2].toString().split(",");

            boolean flag = false;
            for (String myStr : str) {
                if (entry.getValue().equals(myStr)) {
                    flag = true;
                }
            }
            if (flag == false) {
                throw new FieldExist(" Field values doesn't exist, Choose from :-" + temp[2].toString());
            }
        }

        ProductVariation productVariation = new ProductVariation(productVariationModel);
        productVariation.setProduct(product);
        productVariation.setActive(true);

        productVariationRepository.save(productVariation);
        return new ResponseEntity<>(productVariationModel, HttpStatus.CREATED);
    }

    //activate and deactivate

    public ResponseEntity<String> activateProduct(long id) throws UserNotFoundException {
        Product product = productRepository.findById(id);
        User user = userRepository.findById(product.getSeller().getId());
        if (product == null) {
            throw new UserNotFoundException("No product found of Id" + id);
        }

        if (!product.isIs_active()) {
            product.setIs_active(true);
            productRepository.save(product);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Product Activation");
            mailMessage.setText("Your product is activated by admin");
            mailMessage.setTo(user.getEmail());
            emailService.sendEmail(mailMessage);
            return new ResponseEntity<>("Product activated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product is already activated", HttpStatus.OK);
    }

    public ResponseEntity<String> deactivateProduct(long id) throws UserNotFoundException {
        Product product = productRepository.findById(id);
        User user = userRepository.findById(product.getSeller().getId());
        if (product == null) {
            throw new UserNotFoundException("No product found of Id" + id);
        }
        if (product.isIs_active()) {
            product.setIs_active(false);
            productRepository.save(product);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Product Activation");
            mailMessage.setText("Your product is activated by admin");
            mailMessage.setTo(user.getEmail());
            emailService.sendEmail(mailMessage);
            return new ResponseEntity<>("Product deactivated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product is already deactivated", HttpStatus.OK);
    }

    //view product
    public ProductModel viewProductById(long id) throws FieldExist {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new FieldExist("Product Doesn't exist with id : " + id);
        }
        ProductModel productModel = new ProductModel(product);
        return productModel;
    }

    public List<ProductModel> viewAllProducts() {
        List<ProductModel> allProducts = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            ProductModel productModel = new ProductModel(product);
            allProducts.add(productModel);
        }
        return allProducts;
    }

    public ProductVariationModel viewProductVariation(long id) throws FieldExist {

        ProductVariation productVariation = productVariationRepository.findById(id);

        if (productVariation == null) {
            throw new FieldExist("Product Variation exist of id : " + id);

        }

        ProductVariationModel productVariationModel = new ProductVariationModel();
        productVariationModel.setMetadata(productVariation.getMetadata());
        productVariationModel.setPrice(productVariation.getPrice());
        productVariationModel.setQuantity(productVariation.getQuantityAvailable());


        return productVariationModel;
    }


    public List<ProductVariationModel> viewAllVariationOfProduct(long id) throws FieldExist {

        List<ProductVariation> productVariations = productVariationRepository.findAllByProduct(productRepository.findById(id));

        if (productVariations == null) {
            throw new FieldExist("Product Variation does not exist for product  with id :" + id);
        }

        List<ProductVariationModel> allProductVariations = new ArrayList<>();

        for (ProductVariation productVariation : productVariations) {
            ProductVariationModel productVariationModel = new ProductVariationModel();
            productVariationModel.setMetadata(productVariation.getMetadata());
            productVariationModel.setPrice(productVariation.getPrice());
            productVariationModel.setQuantity(productVariation.getQuantityAvailable());
            allProductVariations.add(productVariationModel);
        }
        return allProductVariations;
    }

    //delete product
    public ResponseEntity<String> deleteProduct(long id) throws FieldExist {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new FieldExist("Product does not exist");
        }

//        User user = User.currentUser();
//        if (product.getSeller().getId() != user.getId()) {
//            throw new FieldExist("You can't delete this resource it is not accessible");
//        }
        productRepository.delete(product);
        return new ResponseEntity<String>("Product deleted successfully", HttpStatus.ACCEPTED);
    }

    //update product
    public ResponseEntity<String> updateProduct(long id, ProductModel productModel) throws FieldExist {

        Product product = productRepository.findById(id);

        User user = User.currentUser();
        if (product == null) {
            throw new FieldExist("Product does not exist");
        }
        Seller seller = sellerRepository.findById(user.getId());
        List<Product> sameBrandCategorySeller = productRepository.findSameBrandCategorySeller(product.getBrand(), product.getCategoryId().getId(), user.getId());

        for (Product product1 : sameBrandCategorySeller) {
            if (product1.getName().equals(productModel.getName()))
                throw new FieldExist("This name already exists : ");
        }
        product.setName(productModel.getName());
        product.setDescription(productModel.getDescription());
        product.setIs_returnable(productModel.isIs_returnable());
        product.setIs_cancellable(productModel.isIs_cancellable());

        productRepository.save(product);

        return new ResponseEntity<String>("Product Updated", HttpStatus.OK);
    }

    public ResponseEntity<String> updateProductVariation(long id, ProductVariationModel productVariationModel) throws FieldExist {

        ProductVariation productVariation = productVariationRepository.findById(id);


        if (productVariation == null) {
            throw new FieldExist("Product Variation doesn't exist with id : " + id);
        }


        productVariation.setQuantityAvailable(productVariationModel.getQuantity());
        productVariation.setPrice(productVariationModel.getPrice());
        productVariation.setMetadata(productVariationModel.getMetadata());


        productVariationRepository.save(productVariation);
        return new ResponseEntity<String>("Product Variation updated", HttpStatus.OK);

    }

}


