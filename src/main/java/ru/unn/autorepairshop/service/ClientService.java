package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientUpdatePasswordRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentResponseDto;
import ru.unn.autorepairshop.domain.dto.response.BusyDaysResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.exceptions.UserException;

import java.time.LocalDateTime;

public interface ClientService {

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
    AppointmentCreatedResponseDto createAppointment(AppointmentCreateRequestDto request, String email);

    /**
     * Метод для получения информации о текущем пользователе.
     * <p>
     * Выполняет поиск пользователя по email и возвращает данные о нем.
     * </p>
     *
     * @param email адрес электронной почты текущего пользователя.
     * @return объект {@link ClientInfoResponseDto}, содержащий информацию о пользователе.
     */
    ClientInfoResponseDto getInfoAboutCurrentUser(String email);

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
    ClientInfoUpdateResponseDto updateInfoAboutCurrentUser(ClientInfoUpdateRequestDto request, String email);

    /**
     * Получает все записи на приём для пользователя по его email с пагинацией.
     * <p>
     * Этот метод возвращает страницу объектов типа {@link AppointmentResponseDto},
     * преобразованных из объектов {@link Appointment} для пользователя с указанным email.
     * Пагинация осуществляется через параметр {@link Pageable}.
     *
     * @param of    объект {@link Pageable}, содержащий информацию о странице
     *              (например, номер страницы и количество элементов на странице).
     * @param email email пользователя, для которого необходимо получить записи на приём.
     * @return {@link Page} объектов {@link AppointmentResponseDto}, содержащих записи на приём.
     */
    Page<AppointmentResponseDto> getAllAppointments(Pageable of, String email);

    /**
     * Получает список всех занятых дат для расписания.
     * Этот метод возвращает объект {@link BusyDaysResponseDto}, который содержит
     * список занятых дат в виде объектов {@link LocalDateTime}.
     *
     * @return объект {@link BusyDaysResponseDto}, содержащий список занятых дат.
     */
    BusyDaysResponseDto getAllBusyDays();

    Page<PartOrderResponseDto> getAllPartOrders(Pageable pageable, String name);

    Void updatePassword(ClientUpdatePasswordRequestDto request, String email);
}
