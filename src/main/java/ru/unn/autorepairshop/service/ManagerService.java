package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.unn.autorepairshop.domain.dto.request.AppointmentAddedDateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAddedDateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAddedMechanicResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ManagerViewResponseDto;
import ru.unn.autorepairshop.domain.dto.response.MechanicListResponseDto;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;

public interface ManagerService {

    Page<AppointmentManagerInfoResponseDto> getAllAppointments(String client, AppointmentStatus statusFilter, PageRequest of);

    ManagerViewResponseDto getAppointment(Long id);

    MechanicListResponseDto getAllMechanics();

    AppointmentAddedMechanicResponseDto addMechanicToAppointment(String email, Long appointmentId, Long masterId);

    AppointmentAddedDateResponseDto changeStartDate(String email, Long appointmentId, AppointmentAddedDateRequestDto request);

    AppointmentAddedDateResponseDto changeEndDate(String email, Long appointmentId, AppointmentAddedDateRequestDto request);

}
