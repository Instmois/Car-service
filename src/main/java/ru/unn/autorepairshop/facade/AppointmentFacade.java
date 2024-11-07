package ru.unn.autorepairshop.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.service.ClientService;
import ru.unn.autorepairshop.validator.AppointmentRequestValidatorTemplate;

@RequiredArgsConstructor
@Component
public class AppointmentFacade {

    private final AppointmentRequestValidatorTemplate validator;

    private final ClientService clientService;

    public AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email) {
        validator.validate(request, email);
        return clientService.createAppointment(request, email);
    }

}
