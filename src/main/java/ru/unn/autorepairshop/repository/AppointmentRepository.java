package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unn.autorepairshop.domain.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}