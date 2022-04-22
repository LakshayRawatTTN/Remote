package com.bootcamp.project.ecommerceapplication.services;

import com.bootcamp.project.ecommerceapplication.domain.*;
import com.bootcamp.project.ecommerceapplication.exceptions.UserNotFoundException;
import com.bootcamp.project.ecommerceapplication.models.AddressModel;
import com.bootcamp.project.ecommerceapplication.models.CustomerModel;
import com.bootcamp.project.ecommerceapplication.models.UserModel;
import com.bootcamp.project.ecommerceapplication.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    public Customer addCustomer(CustomerModel customerModel) {

        Customer customer = new Customer(customerModel);
        User user = new User();
        user.setEmail(customerModel.getEmail());
        user.setFirstName(customerModel.getFirstName());
        user.setMiddleName(customerModel.getMiddleName());
        user.setLastName(customerModel.getLastName());
        user.setPassword(passwordEncoder.encode(customerModel.getPassword()));
        List<Role> roles = roleRepository.findAllByIdIn(Arrays.asList(1L));
        user.setRoles(roles);
        customer.setUser(user);
        customerRepository.save(customer);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Complete registration");
        mailMessage.setText("To confirm your account, please click on the given link : "
                + "http://localhost:8080/customer/confirm?token=" + confirmationToken.getConfirmationToken());
        mailMessage.setTo(user.getEmail());


        emailService.sendEmail(mailMessage);

        return customer;

    }

    public List<CustomerModel> getList() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerModel> listCustomer = new ArrayList<>();
        for(Customer customer : customers){
            CustomerModel customerModel = new CustomerModel(customer);
            listCustomer.add(customerModel);
        }
        return listCustomer;
    }

    public ResponseEntity<String> resend(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User Not found");
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Complete registration");
        mailMessage.setText("To confirm your account, please click on the given link : "
                + "http://localhost:8080/users/confirm?token=" + confirmationToken.getConfirmationToken());
        mailMessage.setTo(user.getEmail());

        emailService.sendEmail(mailMessage);
        return new ResponseEntity<String>("Token resended for activation", HttpStatus.OK);
    }

    public ResponseEntity<CustomerModel> viewProfile(String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user != null) {
            CustomerModel customerModel = new CustomerModel();
            customerModel.setEmail(user.getEmail());
            customerModel.setFirstName(user.getFirstName());
            customerModel.setMiddleName(user.getMiddleName());
            customerModel.setLastName(user.getLastName());
            Customer customer = customerRepository.findById(user.getId());
            customerModel.setContact(customer.getContact());
            customerModel.isActive(user.isActive());
            return new ResponseEntity<CustomerModel>(customerModel, HttpStatus.OK);
        }
        {
            throw new UserNotFoundException("customer not found");
        }
    }

    public ResponseEntity<Address> findAddress() {
        User currentUser = User.currentUser();
        User user = userRepository.findByEmail(currentUser.getEmail());
        Address address = user.getAddress();

        return new ResponseEntity<Address>(address, HttpStatus.FOUND);
    }

    public User addAddress(AddressModel addressModel) {
        User user = User.currentUser();
        Address address = new Address(addressModel);
        addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);
        return user;
    }

}
