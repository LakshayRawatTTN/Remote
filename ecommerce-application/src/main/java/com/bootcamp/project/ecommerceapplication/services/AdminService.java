package com.bootcamp.project.ecommerceapplication.services;

import com.bootcamp.project.ecommerceapplication.domain.User;
import com.bootcamp.project.ecommerceapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private EmailService emailService;


    @Transactional
    public boolean activate(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (!user.isActive()) {
                user.setActive(true);
                userRepository.updateUser(email,true);
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Account Activated");
            mailMessage.setText("Your account is successfully activated.");
            mailMessage.setTo(user.getEmail());
            emailService.sendEmail(mailMessage);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deactivate(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                userRepository.updateUser(email,false);
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Account Deactivated");
            mailMessage.setText("Your account is deactivated.");
            mailMessage.setTo(user.getEmail());
            emailService.sendEmail(mailMessage);
            return true;
        }
        return false;
    }

}
