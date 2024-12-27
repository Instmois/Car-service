package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.entity.Vehicle;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.exceptions.VehicleException;
import ru.unn.autorepairshop.repository.VehicleRepository;
import ru.unn.autorepairshop.service.impl.VehicleServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private static final String DEFAULT_LICENSE_PLATE = "ABC1234";
    private static final String DEFAULT_MODEL = "Toyota Corolla";
    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final Long DEFAULT_VEHICLE_ID = 1L;

    private Vehicle vehicle;
    private User client;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword("password");
        authData.setRole(Role.ROLE_CLIENT);

        client = new User();
        client.setAuthData(authData);

        vehicle = Vehicle.builder()
                .id(DEFAULT_VEHICLE_ID)
                .licensePlate(DEFAULT_LICENSE_PLATE)
                .model(DEFAULT_MODEL)
                .client(client)
                .build();
    }

    @Test
    void getByLicensePlateTest() {
        when(vehicleRepository.findByLicensePlate(DEFAULT_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));

        Vehicle foundVehicle = vehicleService.getByLicensePlate(DEFAULT_LICENSE_PLATE);

        assertNotNull(foundVehicle);
        assertEquals(DEFAULT_LICENSE_PLATE, foundVehicle.getLicensePlate());
        assertEquals(DEFAULT_MODEL, foundVehicle.getModel());
        assertEquals(client, foundVehicle.getClient());

        verify(vehicleRepository).findByLicensePlate(DEFAULT_LICENSE_PLATE);
    }

    @Test
    void getByLicensePlateNotFoundTest() {
        when(vehicleRepository.findByLicensePlate(DEFAULT_LICENSE_PLATE)).thenReturn(Optional.empty());

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleService.getByLicensePlate(DEFAULT_LICENSE_PLATE));

        assertEquals(VehicleException.CODE.NO_SUCH_VEHICLE_BY_LICENSE_PLATE, exception.getCode());

        verify(vehicleRepository).findByLicensePlate(DEFAULT_LICENSE_PLATE);
    }

    @Test
    void getOptionalByLicensePlateTest() {
        when(vehicleRepository.findByLicensePlate(DEFAULT_LICENSE_PLATE)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> optionalVehicle = vehicleService.getOptionalByLicensePlate(DEFAULT_LICENSE_PLATE);

        assertTrue(optionalVehicle.isPresent());
        assertEquals(vehicle, optionalVehicle.get());

        verify(vehicleRepository).findByLicensePlate(DEFAULT_LICENSE_PLATE);
    }

    @Test
    void getOptionalByLicensePlateNotFoundTest() {
        when(vehicleRepository.findByLicensePlate(DEFAULT_LICENSE_PLATE)).thenReturn(Optional.empty());

        Optional<Vehicle> optionalVehicle = vehicleService.getOptionalByLicensePlate(DEFAULT_LICENSE_PLATE);

        assertTrue(optionalVehicle.isEmpty());

        verify(vehicleRepository).findByLicensePlate(DEFAULT_LICENSE_PLATE);
    }

    @Test
    void saveTest() {
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle savedVehicle = vehicleService.save(vehicle);

        assertNotNull(savedVehicle);
        assertEquals(DEFAULT_LICENSE_PLATE, savedVehicle.getLicensePlate());
        assertEquals(DEFAULT_MODEL, savedVehicle.getModel());
        assertEquals(client, savedVehicle.getClient());

        verify(vehicleRepository).save(vehicle);
    }
}
