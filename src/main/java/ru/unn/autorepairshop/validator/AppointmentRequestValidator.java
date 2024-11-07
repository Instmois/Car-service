package ru.unn.autorepairshop.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.ServiceType;
import ru.unn.autorepairshop.exceptions.AppointmentException;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AppointmentRequestValidator extends AppointmentRequestValidatorTemplate {

    private final UserService userService;

    private final AppointmentService appointmentService;

    /**
     * Валидация прав собственности пользователя на автомобиль.
     * <p>
     * Проверяет, что текущий пользователь является владельцем автомобиля с указанным номерным знаком.
     * Если автомобиль уже имеет единственного владельца, и этот владелец не является текущим пользователем,
     * то выбрасывается исключение {@link AppointmentException} с кодом {@code CAR_IS_ALREADY_OCCUPIED}.
     * </p>
     *
     * @param request объект запроса для создания записи {@link AppointmentCreateRequestDto}, содержащий информацию о транспортном средстве,
     *                включая его номерной знак.
     * @param email   адрес электронной почты текущего пользователя, пытающегося создать запись.
     * @throws AppointmentException с кодом {@code CAR_IS_ALREADY_OCCUPIED}, если у автомобиля есть единственный
     *                              владелец, и он не совпадает с текущим пользователем.
     *
     *                              <ul>
     *                                  <li>1) Получает всех владельцев автомобиля по номерному знаку из запроса.</li>
     *                                  <li>2) Находит текущего пользователя по переданному адресу электронной почты.</li>
     *                                  <li>3) Проверяет, что если у автомобиля только один владелец и это не текущий пользователь,
     *                                  то выбрасывается исключение, указывающее, что автомобиль занят.</li>
     *                              </ul>
     */
    @Override
    protected void validateUserOwnership(AppointmentCreateRequestDto request, String email) {
        Set<User> users = new HashSet<>(userService.getAllByVehicleLicencePlate(request.licensePlate()));
        User user = userService.getByEmail(email);

        if (!users.contains(user) && users.size() == 1) {
            throwValidationError(AppointmentException.CODE.CAR_IS_ALREADY_OCCUPIED);
        }
    }

    /**
     * Проверка отсутствия существующих записей с аналогичными типами работ для автомобиля.
     * <p>
     * Метод выполняет проверку, чтобы убедиться, что для указанного автомобиля с заданным номерным знаком
     * не существует записей на обслуживание с тем же типом услуги. Если аналогичная запись уже существует,
     * выбрасывается исключение {@link AppointmentException} с кодом {@code SIMILAR_WORKS_EXIST}.
     * </p>
     *
     * @param request объект запроса для создания записи {@link AppointmentCreateRequestDto}, содержащий информацию
     *                о транспортном средстве и типе требуемой услуги.
     *
     * @throws AppointmentException с кодом {@code SIMILAR_WORKS_EXIST}, если существует запись на обслуживание
     *                              с аналогичным типом услуги для указанного автомобиля.
     *
     * <ul>
     *     <li>1) Получает все записи на обслуживание для автомобиля с указанным номерным знаком.</li>
     *     <li>2) Извлекает типы услуг из полученных записей и собирает их в уникальный набор.</li>
     *     <li>3) Проверяет, содержит ли набор существующих типов услуг запрашиваемый тип услуги.
     *         Если да, то выбрасывается исключение, указывающее, что аналогичная запись уже существует.</li>
     * </ul>
     */
    @Override
    protected void validateNoSimilarWorksExist(AppointmentCreateRequestDto request) {
        List<Appointment> appointments = appointmentService.findAllByLicencePlate(request.licensePlate());
        Set<ServiceType> existingTypes = appointments
                .stream()
                .map(Appointment::getServiceType)
                .collect(Collectors.toSet());

        if (existingTypes.contains(request.serviceType())) {
            throwValidationError(AppointmentException.CODE.SIMILAR_WORKS_EXIST);
        }
    }
}
