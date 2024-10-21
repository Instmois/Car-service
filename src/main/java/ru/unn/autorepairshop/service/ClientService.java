package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;

public interface ClientService {

    AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email);

    ClientInfoResponseDto getInfoAboutCurrentUser(String email);

}
