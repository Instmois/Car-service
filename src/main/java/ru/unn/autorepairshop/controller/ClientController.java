package ru.unn.autorepairshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.unn.autorepairshop.controller.api.ClientApi;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientUpdatePasswordRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAllInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.BusyDaysResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.facade.AppointmentFacade;
import ru.unn.autorepairshop.service.ClientService;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
@RestController
public class ClientController implements ClientApi {

    private final AppointmentFacade appointmentFacade;

    private final ClientService clientService;

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping("/appointments")
    public Page<AppointmentAllInfoResponseDto> getAllAppointments(
            Principal principal,
            @PageableDefault(page = 0, size = 6) Pageable pageable
    ) {
        return clientService.getAllAppointments(
                pageable,
                principal.getName()
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping("/busy-days")
    public BusyDaysResponseDto getAllBusyDays() {
        return clientService.getAllBusyDays();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PostMapping("/appointment")
    public AppointmentCreatedResponseDto createAppointment(
            Principal principal,
            @Validated @RequestBody AppointmentCreateRequestDto requestDto
    ) {
        return appointmentFacade.createAppointment(requestDto, principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping("/current")
    public ClientInfoResponseDto getCurrentClient(Principal principal) {
        return clientService.getInfoAboutCurrentUser(principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PutMapping("/current")
    public ClientInfoUpdateResponseDto updateCurrentClient(
            Principal principal,
            @Validated @RequestBody ClientInfoUpdateRequestDto request
    ) {
        return clientService.updateInfoAboutCurrentUser(request, principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping("/part-orders")
    public Page<PartOrderResponseDto> getAllPartOrders(
            Principal principal,
            @PageableDefault(page = 0, size = 6) Pageable pageable
    ) {
        return clientService.getAllPartOrders(pageable, principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PutMapping("/password")
    public Void updateClientPassword(
            Principal principal,
            @Validated @RequestBody ClientUpdatePasswordRequestDto request
    ) {
        return clientService.updatePassword(request, principal.getName());
    }

}
