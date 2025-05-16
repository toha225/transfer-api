package com.transfer.transfer_api.dto;

import jakarta.validation.constraints.Pattern;

public class PhoneUpdateRequest {

    @Pattern(regexp = "^[0-9]{11}$", message = "Неверный формат старого номера телефона")
    private String oldPhone;

    @Pattern(regexp = "^[0-9]{11}$", message = "Неверный формат нового номера телефона")
    private String newPhone;

    public String getOldPhone() {
        return oldPhone;
    }

    public void setOldPhone(String oldPhone) {
        this.oldPhone = oldPhone;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

}
