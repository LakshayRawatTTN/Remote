package com.bootcamp.project.ecommerceapplication.controllers;

import com.bootcamp.project.ecommerceapplication.domain.Customer;
import com.bootcamp.project.ecommerceapplication.domain.Seller;
import com.bootcamp.project.ecommerceapplication.domain.category.Category;
import com.bootcamp.project.ecommerceapplication.domain.category.CategoryMetadataField;
import com.bootcamp.project.ecommerceapplication.domain.category.CategoryMetadataFieldValues;
import com.bootcamp.project.ecommerceapplication.exceptions.FieldExist;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.CategoryMetadataValueModel;
import com.bootcamp.project.ecommerceapplication.models.CategoryModel;
import com.bootcamp.project.ecommerceapplication.models.MetaDataValueModel;
import com.bootcamp.project.ecommerceapplication.models.product.ProductModel;
import com.bootcamp.project.ecommerceapplication.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @Autowired
    SellerService sellerService;

    @Autowired
    AdminService adminService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/getcustomers")
    public List<Customer> getCustomers() {
        return customerService.getList();
    }

    @GetMapping("/getsellers")
    public List<Seller> getSellers() {
        return sellerService.getList();
    }

    @Transactional
    @PutMapping("/activate/customer/{user}")
    public ResponseEntity<String> activateCustomer(@PathVariable String user) {

        if (adminService.activate(user)) {
            return ResponseEntity.accepted().body("Customer activated");
        }
        return new ResponseEntity<>("customer not activated", HttpStatus.NOT_FOUND);
    }
    @Transactional
    @PutMapping("/activate/seller/{user}")
    public ResponseEntity<String> activateSeller(@PathVariable String user) {

        if (adminService.activate(user)) {
            return ResponseEntity.accepted().body("Seller activated");
        }
        return new ResponseEntity<>("seller not found", HttpStatus.NOT_FOUND);
    }

    @Transactional
    @PutMapping("/deactivate/customer/{user}")
    public ResponseEntity<String> deactivateCustomer(@PathVariable String user) {

        if (adminService.deactivate(user)) {
            return ResponseEntity.accepted().body("Customer deactivated");
        }
        return new ResponseEntity<>("customer not found", HttpStatus.NOT_FOUND);
    }

    @Transactional
    @PutMapping("/deactivate/seller/{user}")
    public ResponseEntity<String> deactivateSeller(@PathVariable String user) {

        if (adminService.deactivate(user)) {
            return ResponseEntity.accepted().body("Seller deactivated");
        }
        return new ResponseEntity<>("seller not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add/metadata/{field}")
    public ResponseEntity<CategoryMetadataField> addMetadataField(@PathVariable String field) throws FieldExist {
        return categoryService.addField(field);
    }

    @PostMapping("/add/category/{name}")
    public ResponseEntity<Category> addCategory(@PathVariable String name) throws FieldExist {
        return categoryService.addCategory(name);
    }

    @PostMapping("/add/category/{name}/{id}")
    public Category addCategory(@PathVariable String name,@PathVariable long id) throws FieldExist {
        return categoryService.addCategory(name,id);
    }

    @GetMapping("/metadata/list")
    public List<CategoryMetadataField> getList(){
        return categoryService.getMetaDataFields();
    }

    @GetMapping("/category/{id}")
    public Map<String,List<Category>> getCategory(@PathVariable long id) throws FieldExist {
        return categoryService.viewCategoryById(id);
    }

    @PostMapping("/add/metadatavalue")
    public CategoryMetadataValueModel addMetadataValue(@Valid @RequestBody MetaDataValueModel metadataValues) throws FieldExist {
        return categoryService.addMetadataValue(metadataValues.getCategoryId(), metadataValues.getFieldId(), metadataValues.getValues());
    }

    @PutMapping("/category/update/{id}")
    public Category addCategory(@PathVariable long id,@Valid @RequestBody CategoryModel categoryModel) throws FieldExist {
        return categoryService.updateCategory(id,categoryModel.getName());
    }

    @GetMapping("/category/list")
    public Map<String,List<Object>> getCategoryList(){
        return categoryService.getCategoryList();
    }

    @PutMapping("category/metadatavalue/update")
    public CategoryMetadataValueModel updateMetadataValue(@Valid @RequestBody MetaDataValueModel metaDataValueModel) throws FieldExist {
        return categoryService.updateMetadataValue(metaDataValueModel.getCategoryId(), metaDataValueModel.getFieldId(), metaDataValueModel.getValues());
    }

    @PutMapping("product/activate/{id}")
    public ResponseEntity<String> activateProduct(@PathVariable long id) throws UserNotFoundException {
        return productService.activateProduct(id);
    }

    @PutMapping("product/deactivate/{id}")
    public ResponseEntity<String> deactivateProduct(@PathVariable long id) throws UserNotFoundException {
        return productService.deactivateProduct(id);
    }

    @GetMapping("/product/{id}")
    public ProductModel viewProduct(@PathVariable long id) throws FieldExist {
        return productService.viewProductById(id);
    }

    @GetMapping("/product/list")
    public List<ProductModel> getProductList() throws FieldExist {
        return productService.viewAllProducts();
    }

}
