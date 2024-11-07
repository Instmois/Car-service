package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.JwtRefreshRequestDto;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.security.dto.JwtRequest;
import ru.unn.autorepairshop.security.dto.JwtResponse;
import ru.unn.autorepairshop.security.jwt.JwtCore;
import ru.unn.autorepairshop.service.AuthService;
import ru.unn.autorepairshop.service.UserService;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final JwtCore jwtCore;

    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final User user = userService.getByEmail(loginRequest.getEmail());

        return createJwtResponse(user);
    }

    @Override
    public JwtResponse refresh(JwtRefreshRequestDto refreshToken) {
        return generateTokens(refreshToken.refreshToken());
    }

    @Override
    public JwtResponse register(UserCreateRequestDto registerRequest) {
        User user = userService
                .getByEmail(userService.create(registerRequest).email());

        return createJwtResponse(user);
    }

    private JwtResponse createJwtResponse(User user) {
        return JwtResponse
                .builder()
                .email(user.getAuthData().getEmail())
                .accessToken(jwtCore.createAccessToken(user))
                .refreshToken(jwtCore.createRefreshToken(user))
                .build();
    }

    private JwtResponse generateTokens(String refreshToken) {
        if (!jwtCore.validateRefreshToken(refreshToken)) {
            throw AuthException.CODE.JWT_VALIDATION_ERROR.get();
        }

        Long id = Long.valueOf(jwtCore.getId(refreshToken));
        User user = userService.getById(id);

        return createJwtResponse(user);
    }
}
