package com.bootcamp.project.ecommerceapplication.services;

import com.bootcamp.project.ecommerceapplication.domain.ConfirmationToken;
import com.bootcamp.project.ecommerceapplication.domain.Role;
import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.exceptions.PasswordMismatch;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.PasswordModel;
import com.bootcamp.project.ecommerceapplication.models.UserModel;
import com.bootcamp.project.ecommerceapplication.repositories.RoleRepository;
import com.bootcamp.project.ecommerceapplication.repositories.TokenRepository;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;

    public User save(UserModel userModel) throws PasswordMismatch {
        if(!userModel.getPassword().equals(userModel.getConfirmPassword())){
            throw new PasswordMismatch("password and confirm password are not same");
        }
        User user = new User(userModel);
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        List<Role> roles = roleRepository.findAllByIdIn(userModel.getRoles());
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    public Map<String, Object> getUser(String email) throws UserNotFoundException {
        HashMap<String, Object> response = new HashMap<>();
        User user = userRepository.findByEmail(email);
        if(user!=null){
        response.put("user", user);
        return response;
        }
        throw new UserNotFoundException("user is not registered");
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return authorities;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User Not Found for Email" + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));

    }

    public ResponseEntity<String> forgotPassword(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            ConfirmationToken confirmationToken = new ConfirmationToken(user);
            tokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Forgot Password!");
            mailMessage.setText("To reset your password, please click here : "
                    + "http://localhost:8080/resetpassword?token=" + confirmationToken.getConfirmationToken());


            emailService.sendEmail(mailMessage);
            return new ResponseEntity<String >("mail is send to user account for reset password", HttpStatus.OK);
        } else {
            throw new UserNotFoundException("user not found");
        }


    }

    public ResponseEntity<String> updatePassword(PasswordModel passwordModel) throws PasswordMismatch {
        User user = User.currentUser();
        if(passwordModel.getPassword().equals(passwordModel.getConfirmPassword())){
            user.setPassword(passwordEncoder.encode(passwordModel.getPassword()));
            userRepository.save(user);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("password changed");
            mailMessage.setText("Your password is changed");
            mailMessage.setTo(user.getEmail());
            emailService.sendEmail(mailMessage);
            return new ResponseEntity<String>("password changed",HttpStatus.OK);
        }
        throw new PasswordMismatch("password and confirm password should be same");
    }

}
