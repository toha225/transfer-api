package com.transfer.transfer_api.dto;

import jakarta.validation.constraints.Email;

public class EmailUpdateRequest {

    @Email(message = "Неверный формат старого email")
    private String oldEmail;

    @Email(message = "Неверный формат нового email")
    private String newEmail;

    public String getOldEmail() {
        return oldEmail;
    }

    public void setOldEmail(String oldEmail) {
        this.oldEmail = oldEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

}
