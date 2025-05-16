package com.transfer.transfer_api.controller;

import com.transfer.transfer_api.dto.TransferRequest;
import com.transfer.transfer_api.security.CustomUserDetails;
import com.transfer.transfer_api.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<?> transferMoney(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid TransferRequest request) {
        transferService.transfer(userDetails.id(), request.getToUserId(), request.getAmount());
        return ResponseEntity.ok("Перевод успешно выполнен");
    }
}
