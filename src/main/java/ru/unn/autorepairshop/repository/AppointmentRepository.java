package ru.unn.autorepairshop.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByVehicle_LicensePlate(String licensePlate);

    @Query(value = """
            SELECT a FROM Appointment a
            JOIN FETCH a.client c
            JOIN FETCH c.authData ad
            JOIN FETCH a.vehicle v
            LEFT JOIN FETCH a.schedule s
            LEFT JOIN FETCH s.mechanic m
            WHERE ad.email = :email
            """)
    List<Appointment> findAllByClientEmail(String email);

    @Query(value = """
            SELECT a
            FROM Appointment a
            JOIN FETCH a.client c
            WHERE (:status IS NULL OR a.status = :status)
              AND (:client IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :client, '%')))
            ORDER BY a.appointmentDate DESC
            """)
    List<Appointment> findAllWithFilter(
            @Param("status") AppointmentStatus status,
            @Param("client") String client,
            Pageable pageable
    );

}
