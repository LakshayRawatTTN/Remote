package com.bootcamp.project.ecommerceapplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomerModel {

    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String contact;
    private String Password;

    private boolean isActive;

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
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
}
