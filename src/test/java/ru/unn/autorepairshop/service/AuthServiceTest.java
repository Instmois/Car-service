package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.UserCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.security.dto.JwtRequest;
import ru.unn.autorepairshop.security.dto.JwtResponse;
import ru.unn.autorepairshop.security.jwt.JwtCore;
import ru.unn.autorepairshop.service.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private JwtCore jwtCore;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_NUMBER = "88005553535";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_REFRESH_TOKEN = "refresh_token";
    private static final String DEFAULT_ACCESS_TOKEN = "access_token";
    private static final String DEFAULT_FIRST_NAME = "Ivan";
    private static final String DEFAULT_LAST_NAME = "Ivanov";
    private static final String DEFAULT_PATRONYMIC = "Ivanovich";

    private User user;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword(DEFAULT_PASSWORD);

        user = new User();
        user.setAuthData(authData);
    }

    @Test
    void loginTest() {
        JwtRequest loginRequest = new JwtRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(jwtCore.createAccessToken(user)).thenReturn(DEFAULT_ACCESS_TOKEN);
        when(jwtCore.createRefreshToken(user)).thenReturn(DEFAULT_REFRESH_TOKEN);

        JwtResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(DEFAULT_EMAIL, response.getEmail());
        assertEquals(DEFAULT_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(DEFAULT_REFRESH_TOKEN, response.getRefreshToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(jwtCore).createAccessToken(user);
        verify(jwtCore).createRefreshToken(user);
    }

    @Test
    void registerTest() {
        UserCreateRequestDto registerRequest = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL
        );

        UserCreatedResponseDto userCreatedResponseDto = new UserCreatedResponseDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_EMAIL
        );

        when(userService.create(registerRequest)).thenReturn(userCreatedResponseDto);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(jwtCore.createAccessToken(user)).thenReturn(DEFAULT_ACCESS_TOKEN);
        when(jwtCore.createRefreshToken(user)).thenReturn(DEFAULT_REFRESH_TOKEN);

        JwtResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(DEFAULT_EMAIL, response.getEmail());
        assertEquals(DEFAULT_ACCESS_TOKEN, response.getAccessToken());
        assertEquals(DEFAULT_REFRESH_TOKEN, response.getRefreshToken());

        verify(userService).create(registerRequest);
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(jwtCore).createAccessToken(user);
        verify(jwtCore).createRefreshToken(user);
    }
}
