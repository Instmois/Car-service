package ru.unn.autorepairshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.unn.autorepairshop.controller.api.ManagerApi;
import ru.unn.autorepairshop.domain.dto.request.AppointmentAddedDateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.AppointmentGetAllRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAddedDateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAddedMechanicResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ManagerViewResponseDto;
import ru.unn.autorepairshop.domain.dto.response.MechanicListResponseDto;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.service.ManagerService;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/manager")
@RestController
public class ManagerController implements ManagerApi {

    private final ManagerService managerService;

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/appointments")
    public Page<AppointmentManagerInfoResponseDto> getAllAppointments(
            @Valid AppointmentGetAllRequestDto request
    ) {
        return managerService.getAllAppointments(
                request.getClient(),
                request.getStatusFilter().equalsIgnoreCase("EMPTY")
                        ? null
                        : AppointmentStatus.valueOf(request.getStatusFilter().toUpperCase()),
                PageRequest.of(request.getPageNumber(), request.getPageSize())
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/appointment/{id}")
    public ManagerViewResponseDto getAppointment(@PathVariable Long id) {
        return managerService.getAppointment(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/appointment/masters")
    public MechanicListResponseDto getAllMechanics() {
        return managerService.getAllMechanics();
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/appointment/{appointmentId}/master/{masterId}")
    public AppointmentAddedMechanicResponseDto addMechanicToAppointment(
            Principal principal,
            @PathVariable Long appointmentId,
            @PathVariable Long masterId
    ) {
        return managerService.addMechanicToAppointment(principal.getName(), appointmentId, masterId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/appointment/{appointmentId}/startDate")
    public AppointmentAddedDateResponseDto addStartDateToAppointment(
            Principal principal,
            @PathVariable Long appointmentId,
            AppointmentAddedDateRequestDto request
    ) {
        return managerService.changeStartDate(principal.getName(), appointmentId, request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/appointment/{appointmentId}/endDate")
    public AppointmentAddedDateResponseDto addEndDateToAppointment(
            Principal principal,
            @PathVariable Long appointmentId,
            AppointmentAddedDateRequestDto request
    ) {
        return managerService.changeEndDate(principal.getName(), appointmentId, request);
    }

}
