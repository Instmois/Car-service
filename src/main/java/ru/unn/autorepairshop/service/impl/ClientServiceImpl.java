package ru.unn.autorepairshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.Service;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.entity.Vehicle;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceStatus;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentCreatedResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoUpdateResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.ClientService;
import ru.unn.autorepairshop.service.ServiceService;
import ru.unn.autorepairshop.service.UserService;
import ru.unn.autorepairshop.service.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final AppointmentService appointmentService;

    private final ServiceService serviceService;

    private final AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper;

    private final ClientInfoResponseDtoMapper clientInfoResponseDtoMapper;

    private final ClientInfoUpdateResponseDtoMapper clientInfoUpdateResponseDtoMapper;

    @Autowired
    public ClientServiceImpl(
            UserService userService,
            VehicleService vehicleService,
            AppointmentService appointmentService,
            ServiceService serviceService,
            AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper,
            ClientInfoResponseDtoMapper clientInfoResponseDtoMapper,
            ClientInfoUpdateResponseDtoMapper clientInfoUpdateResponseDtoMapper
    ) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;
        this.serviceService = serviceService;
        this.appointmentCreatedResponseDtoMapper = appointmentCreatedResponseDtoMapper;
        this.clientInfoResponseDtoMapper = clientInfoResponseDtoMapper;
        this.clientInfoUpdateResponseDtoMapper = clientInfoUpdateResponseDtoMapper;
    }

    @Override
    public AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email) {
        User user = userService.getByEmail(email);

        if (vehicleService.getOptionalByLicensePlate(request.licensePlate()).isPresent()) {
            System.out.println(request.licensePlate());
        }

        Vehicle vehicle = vehicleService
                .getOptionalByLicensePlate(request.licensePlate())
                .orElseGet(() -> createVehicleFromRequest(request, user));

        Appointment appointment = appointmentService.save(Appointment.builder()
                .client(user)
                .vehicle(vehicle)
                .status(AppointmentStatus.NEW)
                .appointmentDate(request.appointmentDate())
                .build());

        List<Service> services = request.serviceTypes().stream()
                .map(serviceType -> Service.builder()
                        .serviceType(serviceType)
                        .serviceStatus(ServiceStatus.WAITING)
                        .appointment(appointment)
                        .build())
                .collect(Collectors.toList());

        services.forEach(serviceService::save);

        appointment.setServices(services);

        appointmentService.save(appointment);

        return appointmentCreatedResponseDtoMapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientInfoResponseDto getInfoAboutCurrentUser(String email) {
        User user = userService.getByEmail(email);

        return clientInfoResponseDtoMapper.toDto(user);
    }

    @Override
    public ClientInfoUpdateResponseDto updateInfoAboutCurrentUser(ClientInfoUpdateRequestDto request, String email) {
        User user = userService.getByEmail(email);

        if (request.email() != null && userService.getOptionalByEmail(request.email()).isPresent() && !request.email().equals(email)) {
            throw UserException.CODE.EMAIL_IN_USE.get();
        }

        Optional.ofNullable(request.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(request.patronymic()).ifPresent(user::setPatronymic);
        Optional.ofNullable(request.email()).ifPresent(emailVal -> user.getAuthData().setEmail(emailVal));
        Optional.ofNullable(request.phoneNumber()).ifPresent(user::setPhoneNumber);

        return clientInfoUpdateResponseDtoMapper.toDto(userService.save(user));
    }


    private Vehicle createVehicleFromRequest(AppointmentCreateRequestDto request, User user) {
        Vehicle vehicle = Vehicle.builder()
                .model(request.model())
                .licensePlate(request.licensePlate().toUpperCase())
                .build();

        vehicle.setClient(user);
        user.getVehicles().add(vehicle);

        return vehicleService.save(vehicle);
    }

}
