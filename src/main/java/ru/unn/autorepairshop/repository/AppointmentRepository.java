package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unn.autorepairshop.domain.entity.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByVehicle_LicensePlate(String licensePlate);

}
