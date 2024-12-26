package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.Vehicle;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.exceptions.AppointmentException;
import ru.unn.autorepairshop.repository.AppointmentRepository;
import ru.unn.autorepairshop.service.impl.AppointmentServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private static final Long DEFAULT_APPOINTMENT_ID = 1L;
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final String DEFAULT_LICENSE_PLATE = "ABC123";
    private static final String DEFAULT_VEHICLE_MODEL = "Toyota Camry";
    private static final AppointmentStatus DEFAULT_STATUS = AppointmentStatus.NEW;

    private Appointment appointment;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword("password");
        authData.setRole(Role.ROLE_CLIENT);

        User user = new User();
        user.setAuthData(authData);
        user.setVehicles(new ArrayList<>());

        Vehicle vehicle = Vehicle.builder()
                .model(DEFAULT_VEHICLE_MODEL)
                .licensePlate(DEFAULT_LICENSE_PLATE)
                .client(user)
                .build();

        appointment = new Appointment();
        appointment.setId(DEFAULT_APPOINTMENT_ID);
        appointment.setStatus(DEFAULT_STATUS);
        appointment.setVehicle(vehicle);
    }

    @Test
    void saveTest() {
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment savedAppointment = appointmentService.save(appointment);

        assertNotNull(savedAppointment);
        assertEquals(DEFAULT_APPOINTMENT_ID, savedAppointment.getId());
        assertEquals(DEFAULT_LICENSE_PLATE, savedAppointment.getVehicle().getLicensePlate());

        verify(appointmentRepository).save(appointment);
    }

    @Test
    void findByIdTest() {
        when(appointmentRepository.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(java.util.Optional.of(appointment));

        Appointment foundAppointment = appointmentService.findById(DEFAULT_APPOINTMENT_ID);

        assertNotNull(foundAppointment);
        assertEquals(DEFAULT_APPOINTMENT_ID, foundAppointment.getId());
        assertEquals(DEFAULT_LICENSE_PLATE, foundAppointment.getVehicle().getLicensePlate());

        verify(appointmentRepository).findById(DEFAULT_APPOINTMENT_ID);
    }

    @Test
    void findByIdThrowsExceptionWhenNotFound() {
        when(appointmentRepository.findById(DEFAULT_APPOINTMENT_ID)).thenReturn(java.util.Optional.empty());

        AppointmentException exception = assertThrows(AppointmentException.class, () -> appointmentService.findById(DEFAULT_APPOINTMENT_ID));
        assertEquals(AppointmentException.CODE.APPOINTMENT_IS_NOT_EXIST, exception.getCode());

        verify(appointmentRepository).findById(DEFAULT_APPOINTMENT_ID);
    }

    @Test
    void findAllByLicencePlateTest() {
        when(appointmentRepository.findAllByVehicle_LicensePlate(DEFAULT_LICENSE_PLATE)).thenReturn(List.of(appointment));

        List<Appointment> appointments = appointmentService.findAllByLicencePlate(DEFAULT_LICENSE_PLATE);

        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
        assertEquals(DEFAULT_LICENSE_PLATE, appointments.get(0).getVehicle().getLicensePlate());

        verify(appointmentRepository).findAllByVehicle_LicensePlate(DEFAULT_LICENSE_PLATE);
    }

    @Test
    void findAllByUserTest() {
        Pageable pageable = PageRequest.of(0, 10);
        when(appointmentRepository.findAllByClientEmail(DEFAULT_EMAIL)).thenReturn(List.of(appointment));

        Page<Appointment> appointments = appointmentService.findAllByUser(pageable, DEFAULT_EMAIL);

        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
        assertEquals(DEFAULT_EMAIL, appointments.getContent().get(0).getVehicle().getClient().getAuthData().getEmail());

        verify(appointmentRepository).findAllByClientEmail(DEFAULT_EMAIL);
    }

    @Test
    void findAllWithFilterTest() {
        PageRequest pageRequest = PageRequest.of(0, 10); // Correct parameter type
        when(appointmentRepository.findAllWithFilter(DEFAULT_STATUS, DEFAULT_EMAIL, pageRequest)).thenReturn(List.of(appointment));

        Page<Appointment> appointments = appointmentService.findAllWithFilter(DEFAULT_EMAIL, DEFAULT_STATUS, pageRequest);

        assertNotNull(appointments);
        assertFalse(appointments.isEmpty());
        assertEquals(DEFAULT_EMAIL, appointments.getContent().get(0).getVehicle().getClient().getAuthData().getEmail());
        assertEquals(DEFAULT_STATUS, appointments.getContent().get(0).getStatus());

        verify(appointmentRepository).findAllWithFilter(DEFAULT_STATUS, DEFAULT_EMAIL, pageRequest);
    }

}
