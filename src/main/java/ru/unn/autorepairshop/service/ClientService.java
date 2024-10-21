package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;

public interface ClientService {

    AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email);

}
