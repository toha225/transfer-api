package com.transfer.transfer_api.controller;

import com.transfer.transfer_api.dto.EmailUpdateRequest;
import com.transfer.transfer_api.security.CustomUserDetails;
import com.transfer.transfer_api.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CacheEvict(value = "userSearchApiCache", allEntries = true)
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PutMapping
    public ResponseEntity<?> updateEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid EmailUpdateRequest request) {
        emailService.updateEmail(userDetails.id(), request.getOldEmail(), request.getNewEmail());
        return ResponseEntity.ok("Email обновлён");
    }

    @PostMapping
    public ResponseEntity<?> addEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid EmailUpdateRequest request) {
        emailService.addEmail(userDetails.id(), request.getNewEmail());
        return ResponseEntity.ok("Email добавлен");
    }

    @DeleteMapping
    public ResponseEntity<?> removeEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid EmailUpdateRequest request) {
        emailService.removeEmail(userDetails.id(), request.getOldEmail());
        return ResponseEntity.ok("Email удален");
    }
}
