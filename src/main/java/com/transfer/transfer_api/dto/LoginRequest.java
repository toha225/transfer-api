package com.transfer.transfer_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {

    @Email(message = "Неверный формат email")
    private String email;

    @Pattern(regexp = "^[0-9]{11}$", message = "Неверный формат номера телефона")
    private String phone;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
