package com.bootcamp.project.ecommerceapplication.controllers;

import com.bootcamp.project.ecommerceapplication.domain.ConfirmationToken;
import com.bootcamp.project.ecommerceapplication.domain.Customer;
import com.bootcamp.project.ecommerceapplication.domain.Seller;
import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.exceptions.InvalidTokenException;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.CustomerModel;
import com.bootcamp.project.ecommerceapplication.models.SellerModel;
import com.bootcamp.project.ecommerceapplication.models.UserModel;
import com.bootcamp.project.ecommerceapplication.repositories.TokenRepository;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import com.bootcamp.project.ecommerceapplication.services.CustomerService;
import com.bootcamp.project.ecommerceapplication.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Customer register(@RequestBody CustomerModel customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping(value = "/resend/activation/{email}")
    public ResponseEntity<UserModel> resendActivation(@PathVariable String email) throws UserNotFoundException {
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
            user.setIs_active(true);
            userRepository.save(user);
            return new ResponseEntity<User>(user, HttpStatus.CREATED);
        } else {
            throw new InvalidTokenException("token cannot be null");
        }

    }

}
