package com.bootcamp.project.ecommerceapplication.services;

import com.bootcamp.project.ecommerceapplication.domain.Customer;
import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.domain.ConfirmationToken;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.CustomerModel;
import com.bootcamp.project.ecommerceapplication.models.UserModel;
import com.bootcamp.project.ecommerceapplication.repositories.CustomerRepository;
import com.bootcamp.project.ecommerceapplication.repositories.RoleRepository;
import com.bootcamp.project.ecommerceapplication.repositories.TokenRepository;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    public Customer addCustomer(CustomerModel customerModel){

        Customer customer = new Customer(customerModel);
        User user = new User();
        user.setEmail(customerModel.getEmail());
        user.setFirstName(customerModel.getFirstName());
        user.setMiddleName(customerModel.getMiddleName());
        user.setLastName(customerModel.getLastName());
        user.setPassword(passwordEncoder.encode(customerModel.getPassword()));
        user.setRoles(roleRepository.findAllByIdIn(Arrays.asList(1L)));
        customer.setUser(user);
        customerRepository.save(customer);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Complete registration");
        mailMessage.setText("To confirm your account, please click on the given link : "
                +"http://localhost:8080/users/confirm?token="+confirmationToken.getConfirmationToken());
        mailMessage.setTo(user.getEmail());


        emailService.sendEmail(mailMessage);

        return customer;

    }

    public List<Customer> getList(){
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public ResponseEntity<UserModel> resend(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User Not found");
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Complete registration");
        mailMessage.setText("To confirm your account, please click on the given link : "
                +"http://localhost:8080/users/confirm?token="+confirmationToken.getConfirmationToken());
        mailMessage.setTo(user.getEmail());
        UserModel userModel = new UserModel(user);

        emailService.sendEmail(mailMessage);
        return new ResponseEntity<UserModel>(userModel, HttpStatus.OK);
    }

}
