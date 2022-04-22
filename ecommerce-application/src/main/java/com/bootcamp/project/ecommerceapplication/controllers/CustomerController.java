package com.bootcamp.project.ecommerceapplication.controllers;

import com.bootcamp.project.ecommerceapplication.domain.*;
import com.bootcamp.project.ecommerceapplication.exceptions.FieldExist;
import com.bootcamp.project.ecommerceapplication.exceptions.InvalidTokenException;
import com.bootcamp.project.ecommerceapplication.exceptions.PasswordMismatch;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.*;
import com.bootcamp.project.ecommerceapplication.models.product.ProductModel;
import com.bootcamp.project.ecommerceapplication.repositories.AddressRepository;
import com.bootcamp.project.ecommerceapplication.repositories.CustomerRepository;
import com.bootcamp.project.ecommerceapplication.repositories.TokenRepository;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import com.bootcamp.project.ecommerceapplication.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @PostMapping("/register")
    public Customer register(@RequestBody CustomerModel customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping(value = "/resend/activation/{email}")
    public ResponseEntity<String> resendActivation(@PathVariable String email) throws UserNotFoundException {
        return customerService.resend(email);
    }

    @PutMapping(value = "/confirm")
    public ResponseEntity<User> confirmAcount(@RequestParam("token") String confirmationToken) throws InvalidTokenException {
        ConfirmationToken token = tokenRepository.findByConfirmationToken(confirmationToken);
        System.out.println(token.getUserEntity().getEmail());
        if (token != null) {
            User user = userRepository.findByEmail(token.getUserEntity().getEmail());
            if (user == null) {
                throw new InvalidTokenException("Invalid token");
            }
            user.setActive(true);
            userRepository.save(user);
            return new ResponseEntity<User>(user, HttpStatus.CREATED);
        } else {
            throw new InvalidTokenException("token cannot be null");
        }

    }

    @GetMapping("view/profile/{email}")
    public ResponseEntity<CustomerModel> viewProfile(@PathVariable String email) throws UserNotFoundException {
        return customerService.viewProfile(email);
    }

    @PostMapping("/address")
    public ResponseEntity<User> addAddress(@RequestBody AddressModel addressModel) {
        return ResponseEntity.accepted().body(customerService.addAddress(addressModel));

    }
    @GetMapping("/address/view")
    public ResponseEntity<Address> viewAddress() {
        return customerService.findAddress();
    }

    @PatchMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordModel passwordModel) throws PasswordMismatch {
       return userService.updatePassword(passwordModel);
    }

    @PatchMapping("/profile/update")
    public CustomerModel editProfile( @RequestBody Map<Object, Object> map) {
        User user =  User.currentUser();
        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(User.class,(String)k);
            if(field.getName()=="email" || field.getName()=="password" ||field.getName()=="id") {
                return;
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, v);
        });
        userRepository.save(user);
        Customer customer = customerRepository.findById(user.getId());
        CustomerModel customerModel = new CustomerModel();
        customerModel.setEmail(user.getEmail());
        customerModel.setFirstName(user.getFirstName());
        customerModel.setMiddleName(user.getMiddleName());
        customerModel.setLastName(user.getLastName());
        customerModel.setLastName(user.getLastName());
        customerModel.setContact(customer.getContact());
        return customerModel;
    }

    @DeleteMapping("/address/delete/{id}")
    public String addressDelete(@PathVariable long id ){
        addressRepository.deleteById(id);
        return "Address is Deleted";
    }

    @PatchMapping("/address/update/{id}")
    public AddressModel updateAddress(@PathVariable long id , @RequestBody Map<Object, Object> map) {
        Address address = addressRepository.findById(id);
        map.forEach((k, v) -> {
            Field field = org.springframework.util.ReflectionUtils.findField(Address.class,(String)k);
            if(field.getName()=="id" || field.getName()=="user_id" ||field.getName()=="label"){
                return;
            }
            field.setAccessible(true);
            System.out.println(">>>>>>"+field);
            ReflectionUtils.setField(field, address, v);
        });
        addressRepository.save(address);
        Address updatedAddress = addressRepository.findById(id);
        AddressModel addressModel = new AddressModel(updatedAddress);
        return addressModel;
    }

    @GetMapping("/category/list")
    public Map<String, List<Object>> getCategoryList(){
        return categoryService.getCategoryList();
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
