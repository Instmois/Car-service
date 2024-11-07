package ru.unn.autorepairshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.unn.autorepairshop.controller.api.AuthApi;
import ru.unn.autorepairshop.domain.dto.request.JwtRefreshRequestDto;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.security.dto.JwtRequest;
import ru.unn.autorepairshop.security.dto.JwtResponse;
import ru.unn.autorepairshop.service.AuthService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public JwtResponse register(@RequestBody @Validated UserCreateRequestDto request) {
        return authService.register(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Validated JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody JwtRefreshRequestDto refreshToken) {
        return authService.refresh(refreshToken);
    }

}

