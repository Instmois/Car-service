package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.repository.AppointmentRepository;
import ru.unn.autorepairshop.service.AppointmentService;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findAllByLicencePlate(String licencePlate) {
        return appointmentRepository.findAllByVehicle_LicensePlate(licencePlate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findAllByUser(Pageable of, String email) {
        List<Appointment> result = appointmentRepository.findAllByClientEmail(email);
        return new PageImpl<>(result, of, result.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findAllWithFilter(String client, AppointmentStatus statusFilter, PageRequest of) {
        List<Appointment> result = appointmentRepository.findAllWithFilter(
                statusFilter, client, of
        );
        return new PageImpl<>(result, of, result.size());
    }

}
