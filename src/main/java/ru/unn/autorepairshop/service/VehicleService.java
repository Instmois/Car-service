package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.entity.Vehicle;

import java.util.Optional;

public interface VehicleService {

    Vehicle getByLicensePlate(String licensePlate);

    Optional<Vehicle> getOptionalByLicensePlate(String licensePlate);

    Vehicle save(Vehicle vehicle);
}
