package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ManagerViewResponseDto;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;

public interface ManagerService {

    Page<AppointmentManagerInfoResponseDto> getAllAppointments(String client, AppointmentStatus statusFilter, PageRequest of);

    ManagerViewResponseDto getAppointment(Long id);

}
