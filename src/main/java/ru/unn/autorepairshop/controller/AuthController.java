package ru.unn.autorepairshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unn.autorepairshop.controller.api.AuthApi;
import ru.unn.autorepairshop.domain.dto.request.JwtRefreshRequestDto;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.security.dto.JwtRequest;
import ru.unn.autorepairshop.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserCreateRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated JwtRequest loginRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody JwtRefreshRequestDto refreshToken){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.refresh(refreshToken));
    }

}
