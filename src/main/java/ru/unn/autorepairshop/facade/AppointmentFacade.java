package ru.unn.autorepairshop.facade;

import org.springframework.stereotype.Component;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.service.ClientService;
import ru.unn.autorepairshop.validator.AppointmentRequestValidator;

@Component
public class AppointmentFacade {

    private final AppointmentRequestValidator validator;
    private final ClientService clientService;

    public AppointmentFacade(AppointmentRequestValidator validator, ClientService clientService) {
        this.validator = validator;
        this.clientService = clientService;
    }

    public AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email) {
        validator.validate(request, email);

        return clientService.createAppointment(request, email);
    }
}
