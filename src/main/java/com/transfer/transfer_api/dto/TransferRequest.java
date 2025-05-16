package com.transfer.transfer_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferRequest {

    @NotNull(message = "Идентификатор получателя не может быть пустым")
    private Long toUserId;

    @NotNull(message = "Сумма перевода не может быть пустой")
    @Positive(message = "Сумма перевода должна быть положительной")
    private BigDecimal amount;

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
