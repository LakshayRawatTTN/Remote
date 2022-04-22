package com.bootcamp.project.ecommerceapplication.models;

import javax.validation.constraints.NotBlank;

public class PasswordModel {

    @NotBlank(message = "enter password")
    private String password;
    @NotBlank(message = "enter confirm password")
    private String ConfirmPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
