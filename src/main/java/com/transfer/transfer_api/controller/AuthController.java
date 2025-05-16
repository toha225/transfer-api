package com.transfer.transfer_api.controller;

import com.transfer.transfer_api.dto.JwtResponse;
import com.transfer.transfer_api.dto.LoginRequest;
import com.transfer.transfer_api.security.JwtTokenProvider;
import com.transfer.transfer_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;

    private final JwtTokenProvider tokenProvider;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Принимает учетные данные (например, email/телефон и пароль) и возвращает JWT-токен в случае успешной аутентификации."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Long userId = userService.authenticate(loginRequest);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неверное имя пользователя/телефон или пароль");
        }

        String jwt = tokenProvider.generateToken(userId);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
