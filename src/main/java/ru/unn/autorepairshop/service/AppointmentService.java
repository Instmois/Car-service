package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    Appointment save(Appointment appointment);

    Appointment findById(Long id);

    List<Appointment> findAllByLicencePlate(String licencePlate);

    Page<Appointment> findAllByUser(Pageable of, String email);

    Page<Appointment> findAllWithFilter(String client, AppointmentStatus statusFilter, PageRequest of);

}
