package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    Appointment save(Appointment appointment);

    List<Appointment> findAllByLicencePlate(String licencePlate);

    Page<Appointment> findAllByUser(Pageable of, String email);

}
