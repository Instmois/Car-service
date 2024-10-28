package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    Appointment save(Appointment appointment);

    List<Appointment> findAllByLicencePlate(String licencePlate);

}
