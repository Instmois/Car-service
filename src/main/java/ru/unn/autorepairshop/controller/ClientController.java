package ru.unn.autorepairshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unn.autorepairshop.controller.api.ClientApi;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.AppointmentGetAllRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentResponseDto;
import ru.unn.autorepairshop.facade.AppointmentFacade;
import ru.unn.autorepairshop.service.ClientService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController implements ClientApi {

    private final AppointmentFacade appointmentFacade;

    private final ClientService clientService;

    @Autowired
    public ClientController(
            AppointmentFacade appointmentFacade,
            ClientService clientService
    ) {
        this.appointmentFacade = appointmentFacade;
        this.clientService = clientService;
    }

    @PostMapping("/appointments")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<?> getAllAppointments(Principal principal, @Validated AppointmentGetAllRequestDto requestDto) {
        Page<AppointmentResponseDto> response = clientService.getAllAppointments(
                PageRequest.of(
                        requestDto.pageNumber(),
                        requestDto.pageSize()
                ),
                principal.getName()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/appointment")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<?> createAppointment(Principal principal, @Validated AppointmentCreateRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentFacade.createAppointment(requestDto, principal.getName()));
    }

    @GetMapping("/current")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<?> getCurrentClient(Principal principal) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientService.getInfoAboutCurrentUser(principal.getName()));
    }

    @PutMapping("/current")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<?> updateCurrentClient(Principal principal, @Validated ClientInfoUpdateRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientService.updateInfoAboutCurrentUser(request, principal.getName()));
    }

}
