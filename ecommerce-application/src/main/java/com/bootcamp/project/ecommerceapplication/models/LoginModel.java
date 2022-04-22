package com.bootcamp.project.ecommerceapplication.models;

import javax.validation.constraints.NotBlank;

public class LoginModel {
   @NotBlank(message = "enter username")
   private String username;
   @NotBlank(message = "enter password")
   private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
