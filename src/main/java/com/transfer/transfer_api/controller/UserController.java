package com.transfer.transfer_api.controller;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.transfer.transfer_api.dto.UserSearchRequest;
import com.transfer.transfer_api.entity.document.UserDocument;
import com.transfer.transfer_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Cacheable(
            value = "userSearchApiCache",
            key = "T(java.util.Objects).hash(#name, #email, #phone, #dateOfBirth, #page, #size)"
    )
    @GetMapping
    public ResponseEntity<?> searchUsers(@Valid UserSearchRequest userSearchRequest, BindingResult bindingResult)
            throws IOException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        var searchResponse = userService.searchUsers(userSearchRequest);
        List<UserDocument> users = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

}

