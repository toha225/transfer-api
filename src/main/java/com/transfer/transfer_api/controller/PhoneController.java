package com.transfer.transfer_api.controller;

import com.transfer.transfer_api.dto.PhoneUpdateRequest;
import com.transfer.transfer_api.security.CustomUserDetails;
import com.transfer.transfer_api.service.PhoneService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone")
@CacheEvict(value = "userSearchApiCache", allEntries = true)
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService userService) {
        this.phoneService = userService;
    }

    @PutMapping
    public ResponseEntity<?> updatePhone(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PhoneUpdateRequest request) {
        phoneService.updatePhone(userDetails.id(), request.getOldPhone(), request.getNewPhone());
        return ResponseEntity.ok("Телефон обновлён");
    }

    @PostMapping
    public ResponseEntity<?> addPhone(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PhoneUpdateRequest request) {
        phoneService.addPhone(userDetails.id(), request.getNewPhone());
        return ResponseEntity.ok("Телефон добавлен");
    }

    @DeleteMapping
    public ResponseEntity<?> removePhone(@AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PhoneUpdateRequest request) {
        phoneService.removePhone(userDetails.id(), request.getOldPhone());
        return ResponseEntity.ok("Телефон удален");
    }

}

