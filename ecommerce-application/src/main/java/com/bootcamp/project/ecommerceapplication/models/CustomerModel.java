package com.bootcamp.project.ecommerceapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CustomerModel {

    @Column(unique=true)
    @NotBlank(message = "Enter Your email")
    @Email(message = "Enter a Valid email Address ")
    private String email;
    @NotBlank(message = "first name cannot be empty")
    private String firstName;
    @NotBlank(message = "middle name cannot be null")
    private String middleName;
    @NotBlank(message = "last name cannot be null")
    private String lastName;
    @NotBlank(message = "password is mandatory")
    private String password;
    @NotBlank(message = "it should be same like password")
    private String confirmPassword;
    @NotBlank(message = "Enter Your Contact")
    private String contact;
    private String Password;

    private boolean isActive;



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }



    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isActive(boolean active) {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
