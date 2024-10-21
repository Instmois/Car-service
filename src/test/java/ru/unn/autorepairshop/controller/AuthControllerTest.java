package ru.unn.autorepairshop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import ru.unn.autorepairshop.config.SecurityConfiguration;
import ru.unn.autorepairshop.domain.dto.request.JwtRefreshRequestDto;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.security.dto.JwtRequest;
import ru.unn.autorepairshop.security.dto.JwtResponse;
import ru.unn.autorepairshop.security.jwt.JwtAuthEntryPoint;
import ru.unn.autorepairshop.security.jwt.JwtCore;
import ru.unn.autorepairshop.security.service.UserDetailsServiceImpl;
import ru.unn.autorepairshop.service.AuthService;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
public class AuthControllerTest {
    @MockBean
    private AuthService authService;
    private static final String PATH = "/api/v1/auth";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtCore jwtCore;

    private UserCreateRequestDto userCreateRequest;

    private JwtRequest jwtRequest;

    private JwtRefreshRequestDto jwtRefreshRequest;

    private JwtResponse jwtResponse;

    @MockBean
    protected UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtAuthEntryPoint jwtAuthEntryPoint;

    protected static final String CONTENT_TYPE = "application/json";

    protected static final String DEFAULT_FIRST_NAME = "Ivan";

    protected static final String DEFAULT_LAST_NAME = "Ivanov";

    protected static final String DEFAULT_PATRONYMIC = "Ivanovich";

    protected static final String DEFAULT_EMAIL = "test@example.com";

    private static final String DEFAULT_PASSWORD = "password";

    protected static final String DEFAULT_NUMBER = "88005553535";

    private static final String DEFAULT_REFRESH_TOKEN = "refresh_token";

    private static final String DEFAULT_ACCESS_TOKEN = "access_token";

    @BeforeEach
    void setUp() {
        userCreateRequest = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL
        );

        jwtRequest = new JwtRequest(
                DEFAULT_EMAIL,
                DEFAULT_PASSWORD
        );

        jwtRefreshRequest = new JwtRefreshRequestDto(
                DEFAULT_REFRESH_TOKEN
        );

        jwtResponse = new JwtResponse(
                DEFAULT_EMAIL,
                DEFAULT_ACCESS_TOKEN,
                DEFAULT_REFRESH_TOKEN
        );
    }

    //The beginning of register method tests
    // Test for POST /register - 204 OK
    @Test
    void registerTest() throws Exception {
        when(authService.register(any(UserCreateRequestDto.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/register")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    // Test 400 Bad Request
    @Test
    void registerBadRequestTest() throws Exception {
        UserCreateRequestDto invalidRequest = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD.repeat(2),
                DEFAULT_EMAIL
        );

        when(authService.register(invalidRequest))
                .thenThrow(AuthException.CODE.INVALID_REPEAT_PASSWORD.get());

        mockMvc.perform(post(PATH + "/register")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    //The end of register method tests

    //The beginning of login method tests
    //Test 200 OK
    @Test
    void loginTest() throws Exception {
        when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/login")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    //Test 400 Bad Request
    @Test
    void loginBadRequestTest() throws Exception {
        JwtRequest invalidRequest = new JwtRequest(
                DEFAULT_EMAIL,
                null
        );

        mockMvc.perform(post(PATH + "/login")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    //Test 404 Not Found
    @Test
    void loginNotFoundTest() throws Exception {
        when(authService.login(any(JwtRequest.class)))
                .thenThrow(UserException.CODE.NO_SUCH_USER_EMAIL.get());

        mockMvc.perform(post(PATH + "/login")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isNotFound());
    }
    //The end of login method tests

    //The beginning of refresh method tests
    //Test 200 OK
    @Test
    void refreshTest() throws Exception {
        when(authService.refresh(any(JwtRefreshRequestDto.class))).thenReturn(jwtResponse);

        mockMvc.perform(post(PATH + "/refresh")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    // Test 401 Unauthorized
    @Test
    void refreshUnauthorizedTest() throws Exception {
        when(authService.refresh(any(JwtRefreshRequestDto.class)))
                .thenThrow(AuthException.CODE.JWT_VALIDATION_ERROR.get());

        mockMvc.perform(post(PATH + "/refresh")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequest)))
                .andExpect(status().isUnauthorized());
    }

    //Test 404 Not Found
    @Test
    void refreshNotFoundTest() throws Exception {
        when(authService.refresh(any(JwtRefreshRequestDto.class)))
                .thenThrow(UserException.CODE.NO_SUCH_USER_ID.get());

        mockMvc.perform(post(PATH + "/refresh")
                        .with(csrf())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(jwtRequest)))
                .andExpect(status().isNotFound());
    }
    //The end of login method tests
}
