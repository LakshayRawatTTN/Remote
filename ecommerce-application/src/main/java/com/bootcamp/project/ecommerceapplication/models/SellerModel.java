package com.bootcamp.project.ecommerceapplication.models;

import com.bootcamp.project.ecommerceapplication.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SellerModel {

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
    @Column(unique = true)
    @NotBlank(message = "Enter Your GST")
    private String gst;
    @NotBlank(message = "Enter Your Company Contact")
    private String companyContact;
    @Column(unique = true)
    @NotBlank(message = "Enter Your Company Name")
    private String companyName;

    public SellerModel() {
    }

    public SellerModel(User user) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
