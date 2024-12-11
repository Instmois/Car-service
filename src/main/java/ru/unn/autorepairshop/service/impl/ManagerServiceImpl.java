package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentManagerInfoResponseDtoMapper;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.ManagerService;

@RequiredArgsConstructor
@Transactional
@Service
public class ManagerServiceImpl implements ManagerService {

    private final AppointmentService appointmentService;

    private final AppointmentManagerInfoResponseDtoMapper appointmentManagerInfoResponseDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentManagerInfoResponseDto> getAllAppointments(String client, AppointmentStatus statusFilter, PageRequest of) {
        Page<Appointment> appointments = appointmentService.findAllWithFilter(client, statusFilter, of);
        return appointments.map(appointmentManagerInfoResponseDtoMapper::toDto);
    }

}
