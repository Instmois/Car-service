package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentResponseDto;
import ru.unn.autorepairshop.domain.dto.response.BusyDaysResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.entity.Vehicle;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentCreatedResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoUpdateResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.ClientService;
import ru.unn.autorepairshop.service.ScheduleService;
import ru.unn.autorepairshop.service.UserService;
import ru.unn.autorepairshop.service.VehicleService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@org.springframework.stereotype.Service
public class ClientServiceImpl implements ClientService {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final AppointmentService appointmentService;

    private final ScheduleService scheduleService;

    private final AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper;

    private final ClientInfoResponseDtoMapper clientInfoResponseDtoMapper;

    private final ClientInfoUpdateResponseDtoMapper clientInfoUpdateResponseDtoMapper;

    private final static String DEFAULT_FIELD_STATUS = "В обработке";

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    public AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email) {
        User user = userService.getByEmail(email);

        Vehicle vehicle = vehicleService
                .getOptionalByLicensePlate(request.licensePlate())
                .orElseGet(() -> createVehicleFromRequest(request, user));

        Appointment appointment = appointmentService.save(Appointment.builder()
                .client(user)
                .vehicle(vehicle)
                .status(AppointmentStatus.NEW)
                .appointmentDate(request.appointmentDate())
                .serviceType(request.serviceType())
                .build());

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

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponseDto> getAllAppointments(Pageable pageRequest, String email) {
        Page<Appointment> userAppointments = appointmentService.findAllByUser(pageRequest, email);
        return userAppointments.map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BusyDaysResponseDto getAllBusyDays() {
        return new BusyDaysResponseDto(scheduleService.getAllBusyTimes());
    }

    /**
     * Преобразует Appointment в AppointmentResponseDto в зависимости от статуса заявки.
     */
    private AppointmentResponseDto mapToDto(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.NEW) {
            return new AppointmentResponseDto(
                    DEFAULT_FIELD_STATUS,
                    DEFAULT_FIELD_STATUS,
                    appointment.getServiceType(),
                    appointment.getVehicle().getModel(),
                    appointment.getVehicle().getLicensePlate(),
                    DEFAULT_FIELD_STATUS,
                    appointment.getStatus()
            );
        } else {
            return new AppointmentResponseDto(
                    appointment.getSchedule().getStartDate().format(DATE_TIME_FORMATTER),
                    appointment.getSchedule().getEndDate().format(DATE_TIME_FORMATTER),
                    appointment.getServiceType(),
                    appointment.getVehicle().getModel(),
                    appointment.getVehicle().getLicensePlate(),
                    appointment.getSchedule().getMechanic().getInitials(),
                    appointment.getStatus()
            );
        }
    }


    /**
     * Вспомогательный метод для создания нового автомобиля на основе данных запроса.
     * <p>
     * Метод создает новый объект автомобиля и привязывает его к пользователю, после чего сохраняет его.
     * </p>
     *
     * @param request объект {@link AppointmentCreateRequestDto}, содержащий данные о модели и номере автомобиля.
     * @param user    объект {@link User}, которому будет принадлежать создаваемый автомобиль.
     * @return объект {@link Vehicle}, представляющий созданный автомобиль.
     */
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
