package ru.unn.autorepairshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unn.autorepairshop.controller.api.ClientApi;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.facade.AppointmentFacade;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController implements ClientApi {

    private final AppointmentFacade appointmentFacade;

    @Autowired
    public ClientController(AppointmentFacade appointmentFacade) {
        this.appointmentFacade = appointmentFacade;
    }

    @PostMapping("/appointment")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<?> createAppointment(Principal principal, @Validated AppointmentCreateRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentFacade.createAppointment(requestDto, principal.getName()));
    }

}
