package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;

public interface ClientService {

    AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email);

    ClientInfoResponseDto getInfoAboutCurrentUser(String email);

    ClientInfoUpdateResponseDto updateInfoAboutCurrentUser(ClientInfoUpdateRequestDto request, String email);

    Page<AppointmentResponseDto> getAllAppointments(Pageable of, String email);

}
