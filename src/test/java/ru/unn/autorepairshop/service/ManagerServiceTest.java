package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.unn.autorepairshop.domain.dto.request.AppointmentAddedDateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.*;
import ru.unn.autorepairshop.domain.entity.*;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.exceptions.AppointmentException;
import ru.unn.autorepairshop.service.impl.ManagerServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private MechanicService mechanicService;

    @Mock
    private UserService userService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ManagerServiceImpl managerService;

    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_PASSWORD = "password";
    private static final Long DEFAULT_APPOINTMENT_ID = 1L;
    private static final Long DEFAULT_MECHANIC_ID = 101L;

    private Appointment appointment;
    private User manager;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword(DEFAULT_PASSWORD);
        authData.setRole(Role.ROLE_MANAGER);

        manager = new User();
        manager.setAuthData(authData);

        appointment = new Appointment();
        appointment.setId(DEFAULT_APPOINTMENT_ID);
        appointment.setStatus(AppointmentStatus.NEW);

        Schedule schedule = new Schedule();
        schedule.setAppointment(appointment);

        appointment.setSchedule(schedule);
    }

    @Test
    void getAllMechanics() {
        MechanicListResponseDto expectedResponse = new MechanicListResponseDto(List.of());
        when(mechanicService.getAllMechanics()).thenReturn(expectedResponse);

        MechanicListResponseDto response = managerService.getAllMechanics();

        assertNotNull(response);
        verify(mechanicService).getAllMechanics();
    }

    @Test
    void addMechanicToAppointment() {
        AppointmentAddedMechanicResponseDto expectedResponse = new AppointmentAddedMechanicResponseDto(DEFAULT_APPOINTMENT_ID, DEFAULT_MECHANIC_ID);

        Mechanic mechanic = new Mechanic();
        mechanic.setId(DEFAULT_MECHANIC_ID);

        when(appointmentService.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(appointment);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(manager);
        when(mechanicService.getMechanicById(DEFAULT_MECHANIC_ID)).thenReturn(mechanic);

        AppointmentAddedMechanicResponseDto response = managerService.addMechanicToAppointment(DEFAULT_EMAIL, DEFAULT_APPOINTMENT_ID, DEFAULT_MECHANIC_ID);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(appointmentService).findById(DEFAULT_APPOINTMENT_ID);
        verify(userService).getByEmail(DEFAULT_EMAIL);
        verify(mechanicService).getMechanicById(DEFAULT_MECHANIC_ID);
        verify(scheduleService).save(any(Schedule.class));
        verify(appointmentService).save(any(Appointment.class));
    }

    @Test
    void addMechanicToAppointment_throwsAppointmentException_ifScheduleNotAssigned() {
        when(appointmentService.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(appointment);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(manager);
        appointment.setSchedule(null);

        AppointmentException exception = assertThrows(AppointmentException.class, () -> managerService.addMechanicToAppointment(DEFAULT_EMAIL, DEFAULT_APPOINTMENT_ID, DEFAULT_MECHANIC_ID));
        assertEquals(AppointmentException.CODE.MECHANIC_IS_NOT_ASSIGNED, exception.getCode());
    }

    @Test
    void changeStartDate() {
        AppointmentAddedDateRequestDto request = new AppointmentAddedDateRequestDto(LocalDateTime.now().plusDays(1));
        when(appointmentService.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(appointment);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(manager);

        AppointmentAddedDateResponseDto response = managerService.changeStartDate(DEFAULT_EMAIL, DEFAULT_APPOINTMENT_ID, request);

        assertNotNull(response);
        verify(appointmentService).findById(DEFAULT_APPOINTMENT_ID);
        verify(userService).getByEmail(DEFAULT_EMAIL);
    }

    @Test
    void changeStartDate_throwsAppointmentException_ifDateIsWrong() {
        AppointmentAddedDateRequestDto request = new AppointmentAddedDateRequestDto(LocalDateTime.now().minusDays(1));
        when(appointmentService.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(appointment);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(manager);

        AppointmentException exception = assertThrows(AppointmentException.class, () -> managerService.changeStartDate(DEFAULT_EMAIL, DEFAULT_APPOINTMENT_ID, request));
        assertEquals(AppointmentException.CODE.WRONG_DATE, exception.getCode());
    }

    @Test
    void changeEndDate() {
        AppointmentAddedDateRequestDto request = new AppointmentAddedDateRequestDto(LocalDateTime.now().plusDays(2));
        when(appointmentService.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(appointment);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(manager);

        AppointmentAddedDateResponseDto response = managerService.changeEndDate(DEFAULT_EMAIL, DEFAULT_APPOINTMENT_ID, request);

        assertNotNull(response);
        verify(appointmentService).findById(DEFAULT_APPOINTMENT_ID);
        verify(userService).getByEmail(DEFAULT_EMAIL);
    }

    @Test
    void changeEndDate_throwsAppointmentException_ifDateIsWrong() {
        AppointmentAddedDateRequestDto request = new AppointmentAddedDateRequestDto(LocalDateTime.now().minusDays(1));
        when(appointmentService.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(appointment);
        when(userService.getByEmail(DEFAULT_EMAIL)).thenReturn(manager);

        AppointmentException exception = assertThrows(AppointmentException.class, () -> managerService.changeEndDate(DEFAULT_EMAIL, DEFAULT_APPOINTMENT_ID, request));
        assertEquals(AppointmentException.CODE.WRONG_DATE, exception.getCode());
    }
}
