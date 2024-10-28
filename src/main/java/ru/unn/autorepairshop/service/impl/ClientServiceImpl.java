package ru.unn.autorepairshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
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
import ru.unn.autorepairshop.service.UserService;
import ru.unn.autorepairshop.service.VehicleService;

import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final UserService userService;

    private final VehicleService vehicleService;

    private final AppointmentService appointmentService;

    private final AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper;

    private final ClientInfoResponseDtoMapper clientInfoResponseDtoMapper;

    private final ClientInfoUpdateResponseDtoMapper clientInfoUpdateResponseDtoMapper;

    @Autowired
    public ClientServiceImpl(
            UserService userService,
            VehicleService vehicleService,
            AppointmentService appointmentService,
            AppointmentCreatedResponseDtoMapper appointmentCreatedResponseDtoMapper,
            ClientInfoResponseDtoMapper clientInfoResponseDtoMapper,
            ClientInfoUpdateResponseDtoMapper clientInfoUpdateResponseDtoMapper
    ) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;
        this.appointmentCreatedResponseDtoMapper = appointmentCreatedResponseDtoMapper;
        this.clientInfoResponseDtoMapper = clientInfoResponseDtoMapper;
        this.clientInfoUpdateResponseDtoMapper = clientInfoUpdateResponseDtoMapper;
    }

    /**
     * Метод для создания новой заявки на выполнение услуги.
     * <p>
     * Метод создает заявку для указанного пользователя на основе данных из запроса.
     * Если указанного автомобиля у пользователя нет, метод создает новый автомобиль на основе данных запроса.
     * </p>
     *
     * @param request объект {@link AppointmentCreateRequestDto}, содержащий информацию о создаваемой заявке,
     *                включая тип услуги, дату и номерной знак автомобиля.
     * @param email   адрес электронной почты текущего пользователя, для которого создается заявка.
     * @return объект {@link AppointmentCreatedResponseDto}, представляющий данные созданной заявки.
     */
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

    /**
     * Метод для получения информации о текущем пользователе.
     * <p>
     * Выполняет поиск пользователя по email и возвращает данные о нем.
     * </p>
     *
     * @param email адрес электронной почты текущего пользователя.
     * @return объект {@link ClientInfoResponseDto}, содержащий информацию о пользователе.
     */
    @Override
    @Transactional(readOnly = true)
    public ClientInfoResponseDto getInfoAboutCurrentUser(String email) {
        User user = userService.getByEmail(email);

        return clientInfoResponseDtoMapper.toDto(user);
    }

    /**
     * Метод для обновления информации о текущем пользователе.
     * <p>
     * Обновляет данные пользователя на основе запроса. При попытке смены email
     * проверяет, что новый email не используется другим пользователем. Обновленные данные сохраняются.
     * </p>
     *
     * @param request объект {@link ClientInfoUpdateRequestDto}, содержащий новые данные пользователя.
     * @param email   текущий email пользователя для идентификации.
     * @return объект {@link ClientInfoUpdateResponseDto}, представляющий обновленные данные пользователя.
     * @throws UserException с кодом {@code EMAIL_IN_USE}, если новый email уже используется другим пользователем.
     */
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
