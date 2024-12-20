package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientUpdatePasswordRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAllInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentDateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.BusyDaysResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.PartOrder;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.entity.Vehicle;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentCreatedResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.client.ClientInfoUpdateResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.partorder.PartOrderResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.ClientService;
import ru.unn.autorepairshop.service.PartOrderService;
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

    private final PartOrderService partOrderService;

    private final PasswordEncoder passwordEncoder;

    private final AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper;

    private final PartOrderResponseDtoMapper partOrderResponseDtoMapper;

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
    public Page<AppointmentAllInfoResponseDto> getAllAppointments(Pageable pageRequest, String email) {
        Page<Appointment> userAppointments = appointmentService.findAllByUser(pageRequest, email);
        return userAppointments.map(this::mapAppointmentToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BusyDaysResponseDto getAllBusyDays() {
        return new BusyDaysResponseDto(scheduleService.getAllBusyTimes());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartOrderResponseDto> getAllPartOrders(Pageable pageRequest, String email) {
        Page<PartOrder> partOrders = partOrderService.findAllByUserEmail(email, pageRequest);
        return partOrders.map(partOrderResponseDtoMapper::mapPartOrderToDto);
    }

    @Override
    public Void updatePassword(ClientUpdatePasswordRequestDto request, String email) {
        User user = userService.getByEmail(email);

        if (!passwordEncoder.matches(request.oldPassword(), user.getAuthData().getPassword())) {
            throw AuthException.CODE.INVALID_OLD_PASSWORD.get();
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw AuthException.CODE.INVALID_REPEAT_PASSWORD.get();
        }

        if (request.oldPassword().equals(request.newPassword())) {
            throw AuthException.CODE.OLD_PASSWORD_EQUALS_TO_NEW_PASSWORD.get();
        }

        user.getAuthData().setPassword(passwordEncoder.encode(request.newPassword()));

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDateResponseDto getAppointmentDate(Long id) {
        return new AppointmentDateResponseDto(
                appointmentService
                        .findById(id).getAppointmentDate().format(DATE_TIME_FORMATTER)
        );
    }

    /**
     * Преобразует Appointment в AppointmentResponseDto.
     */
    private AppointmentAllInfoResponseDto mapAppointmentToDto(Appointment appointment) {
        return new AppointmentAllInfoResponseDto(
                appointment.getId(),
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getStartDate() != null
                        ? appointment.getSchedule().getStartDate().format(DATE_TIME_FORMATTER)
                        : DEFAULT_FIELD_STATUS
                        : DEFAULT_FIELD_STATUS,
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getEndDate() != null
                        ? appointment.getSchedule().getEndDate().format(DATE_TIME_FORMATTER)
                        : DEFAULT_FIELD_STATUS
                        : DEFAULT_FIELD_STATUS,
                appointment.getServiceType(),
                appointment.getVehicle().getModel(),
                appointment.getVehicle().getLicensePlate(),
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getMechanic() != null
                        ? appointment.getSchedule().getMechanic().getInitials()
                        : DEFAULT_FIELD_STATUS
                        : DEFAULT_FIELD_STATUS,
                appointment.getStatus()
        );
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
