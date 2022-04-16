package com.bootcamp.project.ecommerceapplication.controllers;

import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.exceptions.InvalidTokenException;
import com.bootcamp.project.ecommerceapplication.exceptions.PasswordMismatch;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.domain.ConfirmationToken;
import com.bootcamp.project.ecommerceapplication.models.LoginModel;
import com.bootcamp.project.ecommerceapplication.models.UserModel;
import com.bootcamp.project.ecommerceapplication.repositories.TokenRepository;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import com.bootcamp.project.ecommerceapplication.services.UserService;
import com.bootcamp.project.ecommerceapplication.utils.JwtUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserModel user) throws PasswordMismatch {
        return userService.save(user);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Map> getUser(@PathVariable String email) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(email), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody LoginModel model) {

        HashMap<String, Object> response = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(model.getUsername(), model.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.put("token", JwtUtils.create(authentication));

            return ResponseEntity.accepted().body(response);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/currentuser")
    public User currentUser() {
        return User.currentUser();
    }


    @PostMapping(value = {"/forgotpassword"})
    public ResponseEntity<User> forgotPassword(@RequestBody ObjectNode objectNode) throws UserNotFoundException {
        String email = objectNode.get("email").asText();
        return userService.forgotPassword(email);
    }

//    @PutMapping(value = {"/resetpassword"})
//    public void resetPassword(@RequestParam("token") String confirmationToken,@RequestBody ObjectNode objectNode) {
//        ConfirmationToken token = tokenRepository.findByConfirmationToken(confirmationToken);
//        if (token != null) {
//            User user = userRepository.findByEmail(token.getUserEntity().getEmail());
//            String newPassword = objectNode.get("newPassword").asText();
//            String confirmPassword = objectNode.get("confirmPassword").asText();
//            if(newPassword.equals(confirmPassword)){
//                user.setPassword(passwordEncoder.encode(newPassword));
//                userRepository.save(user);
//            }
//            else {
//                System.out.println("New Password and Confirm Password do not match");
//            }
//
//        } else {
//            System.out.println("token invalid");
//        }
//
//    }

}

