package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.unn.autorepairshop.domain.entity.Appointment;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByVehicle_LicensePlate(String licensePlate);

    @Query(value = """
            SELECT a FROM Appointment a
            JOIN FETCH a.client c
            JOIN FETCH c.authData ad
            JOIN FETCH a.vehicle v
            JOIN FETCH a.schedule s
            JOIN FETCH s.mechanic m
            WHERE ad.email = :email
            """)
    List<Appointment> findAllByClientEmail(String email);

}
