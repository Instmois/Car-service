package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.UserCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.domain.mapper.user.UserCreateRequestDtoMapper;
import ru.unn.autorepairshop.domain.mapper.user.UserCreateResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.repository.UserRepository;
import ru.unn.autorepairshop.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCreateRequestDtoMapper userCreateRequestDtoMapper;

    @Mock
    private UserCreateResponseDtoMapper userCreateResponseDtoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String DEFAULT_FIRST_NAME = "Ivan";
    private static final String DEFAULT_LAST_NAME = "Ivanov";
    private static final String DEFAULT_PATRONYMIC = "Ivanovich";
    private static final String DEFAULT_NUMBER = "88005553535";
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded_password";
    private static final Long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_LICENSE_PLATE = "A000AA";

    private User user;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword(ENCODED_PASSWORD);
        authData.setRole(Role.ROLE_CLIENT);

        user = new User();
        user.setAuthData(authData);
    }

    @Test
    void getById_shouldReturnUser() {
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(user));

        User result = userService.getById(DEFAULT_USER_ID);

        assertNotNull(result);
        assertEquals(DEFAULT_EMAIL, result.getAuthData().getEmail());
        verify(userRepository).findById(DEFAULT_USER_ID);
    }

    @Test
    void getById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.getById(DEFAULT_USER_ID));
        verify(userRepository).findById(DEFAULT_USER_ID);
    }

    @Test
    void getByEmail_shouldReturnUser() {
        when(userRepository.findByAuthData_Email(DEFAULT_EMAIL)).thenReturn(Optional.of(user));

        User result = userService.getByEmail(DEFAULT_EMAIL);

        assertNotNull(result);
        assertEquals(DEFAULT_EMAIL, result.getAuthData().getEmail());
        verify(userRepository).findByAuthData_Email(DEFAULT_EMAIL);
    }

    @Test
    void getByEmail_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByAuthData_Email(DEFAULT_EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.getByEmail(DEFAULT_EMAIL));
        verify(userRepository).findByAuthData_Email(DEFAULT_EMAIL);
    }

    @Test
    void create_shouldReturnUser() {
        UserCreateRequestDto request = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL
        );

        UserCreatedResponseDto responseDto = new UserCreatedResponseDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_EMAIL
        );

        when(userCreateRequestDtoMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(DEFAULT_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.findByAuthData_Email(DEFAULT_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userCreateResponseDtoMapper.toDto(user)).thenReturn(responseDto);

        UserCreatedResponseDto result = userService.create(request);

        assertNotNull(result);
        verify(userRepository).findByAuthData_Email(DEFAULT_EMAIL);
        verify(passwordEncoder).encode(DEFAULT_PASSWORD);
        verify(userRepository).save(user);
    }

    @Test
    void create_shouldThrowException_whenEmailExists() {
        UserCreateRequestDto request = new UserCreateRequestDto(
                DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME,
                DEFAULT_PATRONYMIC,
                DEFAULT_NUMBER,
                DEFAULT_PASSWORD,
                DEFAULT_PASSWORD,
                DEFAULT_EMAIL
        );

        when(userRepository.findByAuthData_Email(DEFAULT_EMAIL)).thenReturn(Optional.of(user));

        assertThrows(AuthException.class, () -> userService.create(request));
        verify(userRepository).findByAuthData_Email(DEFAULT_EMAIL);
    }

    @Test
    void getAllByVehicleLicencePlate_shouldReturnUsers() {
        when(userRepository.findAllByVehicleLicencePlate(DEFAULT_LICENSE_PLATE)).thenReturn(List.of(user));

        List<User> result = userService.getAllByVehicleLicencePlate(DEFAULT_LICENSE_PLATE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DEFAULT_EMAIL, result.get(0).getAuthData().getEmail());
        verify(userRepository).findAllByVehicleLicencePlate(DEFAULT_LICENSE_PLATE);
    }
}
