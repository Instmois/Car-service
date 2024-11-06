package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unn.autorepairshop.domain.entity.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

}
