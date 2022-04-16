package com.bootcamp.project.ecommerceapplication.controllers;

import com.bootcamp.project.ecommerceapplication.domain.Address;
import com.bootcamp.project.ecommerceapplication.domain.Seller;
import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.exceptions.PasswordMismatch;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.AddressModel;
import com.bootcamp.project.ecommerceapplication.models.PasswordModel;
import com.bootcamp.project.ecommerceapplication.models.SellerModel;
import com.bootcamp.project.ecommerceapplication.repositories.AddressRepository;
import com.bootcamp.project.ecommerceapplication.repositories.SellerRepository;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import com.bootcamp.project.ecommerceapplication.services.SellerService;
import com.bootcamp.project.ecommerceapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.util.Map;

@RestController
@RequestMapping("/seller")
public class SellerController {


    @Autowired
    UserRepository userRepository;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping("/register")
    public ResponseEntity<SellerModel> register(@RequestBody SellerModel seller) {
        return sellerService.addSeller(seller);
    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<Map> viewProfile(@PathVariable String email) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
    }


    //new api
    @PostMapping("/address")
    public ResponseEntity<User> addAddress(@RequestBody AddressModel addressModel) {
        return ResponseEntity.accepted().body(sellerService.addAddress(addressModel));

    }


    @GetMapping("/address/view")
    public ResponseEntity<Address> viewAddress() {
        return sellerService.findAddress();
    }

    @PatchMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordModel passwordModel) throws PasswordMismatch {
        return userService.updatePassword(passwordModel);
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

    @PatchMapping("/profile/update")
    public SellerModel editProfile( @RequestBody Map<Object, Object> map) {
        User user = User.currentUser();
        System.out.println("hello");
        //UserEntity user = userRepo.findByEmail(email);
        map.forEach((k, v) -> {
            
            Field field = org.springframework.util.ReflectionUtils.findField(User.class,(String)k);
            if(field.getName()=="email" || field.getName()=="password" ||field.getName()=="id") {
                return;
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, v);
        });
        System.out.println("hello");
        System.out.println("hello");

        userRepository.save(user);
        System.out.println("hello");
        System.out.println("hello");


        Seller seller = sellerRepository.findById(user.getId());
        SellerModel sellerModel = new SellerModel(user);
        sellerModel.setGst(seller.getGst());
        sellerModel.setCompanyName(seller.getCompanyName());
        sellerModel.setCompanyContact(seller.getCompanyContact());
        return sellerModel;
    }
}
