package ru.unn.autorepairshop.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import ru.unn.autorepairshop.domain.dto.request.AppointmentGetAllRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.exceptions.message.ErrorMessage;

@Tag(name = "Manager API", description = "API для работы менеджеров")
public interface ManagerApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppointmentManagerInfoResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизирован",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(
            summary = """
                    Получение всех заявок. Метод возвращает все заявки, которые 
                    есть в бд. У него есть два необязательных параметра: 
                    client (фамилия клиента, по которой может идти выборка, по 
                    умолчанию по фамилии выборки нет) и statusFilter. Он принимает 
                    4 значения: empty (параметр по умолчанию, если он есть, то 
                    выборки по статусу работы нет), new (параметр, который будет 
                    выдавать только записи со статусом "Новая"), at_work (параметр, 
                    который будет выдавать только записи со статусом "В процессе") 
                    и done (параметр, который будет выдавать только записи со 
                    статусом "Завершена").
                    """
    )
    @GetMapping("/appointments")
    Page<AppointmentManagerInfoResponseDto> getAllAppointments(@Valid AppointmentGetAllRequestDto request);

}
