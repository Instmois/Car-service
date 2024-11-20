package ru.unn.autorepairshop.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientUpdatePasswordRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentResponseDto;
import ru.unn.autorepairshop.domain.dto.response.BusyDaysResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.exceptions.message.ErrorMessage;
import ru.unn.autorepairshop.exceptions.message.ValidationErrorMessage;

import java.security.Principal;

@Tag(name = "Client API", description = "API для работы клиентов")
public interface ClientApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppointmentResponseDto.class)
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
    @Operation(summary = "Получение всех заявок пользователя")
    @GetMapping("/appointments")
    Page<AppointmentResponseDto> getAllAppointments(Principal principal, Pageable pageable);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BusyDaysResponseDto.class)
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
    @Operation(summary = "Получение информации по занятым датам")
    @GetMapping("/busy-days")
    BusyDaysResponseDto getAllBusyDays();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание заявки на выполнение работ",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppointmentCreatedResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных параметров",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorMessage.class)
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному email не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Формирование заявки на работу сервиса")
    @PostMapping("/appointment")
    AppointmentCreatedResponseDto createAppointment(
            Principal principal,
            @RequestBody @Validated AppointmentCreateRequestDto requestDto
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientInfoResponseDto.class)
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному email не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получение информации о текущем пользователе")
    @GetMapping("/current")
    ClientInfoResponseDto getCurrentClient(
            Principal principal
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientInfoUpdateResponseDto.class)
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь по переданному email не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Данный email используется",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Обновление информации о текущем пользователе")
    @PutMapping("/current")
    ClientInfoUpdateResponseDto updateCurrentClient(
            Principal principal,
            @RequestBody @Validated ClientInfoUpdateRequestDto request
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение информации",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = PartOrderResponseDto.class)
                                    )
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
    @Operation(summary = "Получение списка всех заказанных деталей")
    @GetMapping("/part-orders")
    Page<PartOrderResponseDto> getAllPartOrders(
            Principal principal,
            Pageable pageable
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление пароля",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema()
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Обновление пароля пользователя")
    @PutMapping("/password")
    Void updateClientPassword(
            Principal principal,
            @Validated @RequestBody ClientUpdatePasswordRequestDto request
    );

}
