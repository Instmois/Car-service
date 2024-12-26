package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ru.unn.autorepairshop.domain.dto.request.*;
import ru.unn.autorepairshop.domain.dto.response.*;
import ru.unn.autorepairshop.domain.entity.*;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.domain.enums.ServiceType;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentCreatedResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.service.impl.ClientServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper;

    @Mock
    private ClientInfoResponseDtoMapper clientInfoResponseDtoMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private static final String DEFAULT_FIRST_NAME = "Ivan";
    private static final String DEFAULT_LAST_NAME = "Ivanov";
    private static final String DEFAULT_PATRONYMIC = "Ivanovich";
    private static final String DEFAULT_PHONE = "88005553535";
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String DEFAULT_NEW_PASSWORD = "newPassword";
    private static final String DEFAULT_VEHICLE_MODEL = "Tesla";

    private static final String DEFAULT_LICENSE_PLATE = "A000AA";

    private User user;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword(DEFAULT_PASSWORD);
        authData.setRole(Role.ROLE_CLIENT);

        user = new User();
        user.setAuthData(authData);
        user.setVehicles(new ArrayList<>());

        vehicle = Vehicle.builder().model(DEFAULT_VEHICLE_MODEL).licensePlate(DEFAULT_LICENSE_PLATE).client(user).build();
    }

    @Test
    void createAppointment() {
        LocalDateTime appointmentTime = LocalDateTime.now();
        AppointmentCreateRequestDto request =
                new AppointmentCreateRequestDto(
                        DEFAULT_LICENSE_PLATE, DEFAULT_VEHICLE_MODEL,
                        appointmentTime, ServiceType.DIAGNOSTIC);

        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(vehicleService.getOptionalByLicensePlate(DEFAULT_LICENSE_PLATE)).thenReturn(Optional.empty());
        when(vehicleService.save(any(Vehicle.class))).thenReturn(vehicle);
        when(appointmentService.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentCreatedResponseDto expectedResponse =
                new AppointmentCreatedResponseDto(DEFAULT_LICENSE_PLATE,
                        DEFAULT_VEHICLE_MODEL, AppointmentStatus.NEW,
                        appointmentTime, ServiceType.DIAGNOSTIC);
        when(appointmentCreatedResponseDtoMapper.toDto(any(Appointment.class))).thenReturn(expectedResponse);

        AppointmentCreatedResponseDto response = clientService.createAppointment(request, DEFAULT_EMAIL);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(vehicleService).getOptionalByLicensePlate(DEFAULT_LICENSE_PLATE);
        verify(vehicleService).save(any(Vehicle.class));
//        verify(appointmentService).save(any(Appointment.class));
        verify(appointmentCreatedResponseDtoMapper).toDto(any(Appointment.class));
    }

    @Test
    void updatePassword() {
        ClientUpdatePasswordRequestDto request = new ClientUpdatePasswordRequestDto(DEFAULT_PASSWORD, DEFAULT_NEW_PASSWORD, DEFAULT_NEW_PASSWORD);

        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(passwordEncoder.matches(DEFAULT_PASSWORD, DEFAULT_PASSWORD)).thenReturn(true);

        clientService.updatePassword(request, DEFAULT_EMAIL);

        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(passwordEncoder).matches(DEFAULT_PASSWORD, DEFAULT_PASSWORD);
        verify(passwordEncoder).encode(DEFAULT_NEW_PASSWORD);
    }

    @Test
    void updatePasswordInvalidOld() {
        ClientUpdatePasswordRequestDto request = new ClientUpdatePasswordRequestDto("wrongPassword", DEFAULT_NEW_PASSWORD, DEFAULT_NEW_PASSWORD);

        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", DEFAULT_PASSWORD)).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> clientService.updatePassword(request, DEFAULT_EMAIL));

        assertEquals(AuthException.CODE.INVALID_OLD_PASSWORD, exception.getCode());
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(passwordEncoder).matches("wrongPassword", DEFAULT_PASSWORD);
    }

    @Test
    void getInfoAboutCurrentUser() {
        ClientInfoResponseDto expectedResponse = new ClientInfoResponseDto(DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_PATRONYMIC, DEFAULT_PHONE, DEFAULT_EMAIL);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(user);
        when(clientInfoResponseDtoMapper.toDto(user)).thenReturn(expectedResponse);

        ClientInfoResponseDto response = clientService.getInfoAboutCurrentUser(DEFAULT_EMAIL);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(clientInfoResponseDtoMapper).toDto(user);
    }
}
