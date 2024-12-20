package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response с неточной датой заявки")
public record AppointmentDateResponseDto(

        @Schema(description = "Неточная дата", example = "example = \"2024-11-06T10:00:00\"")
        String date

) {
}
