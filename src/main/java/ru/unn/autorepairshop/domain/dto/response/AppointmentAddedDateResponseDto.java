package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response ответ на запрос о добавлении даты к заявке")
public record AppointmentAddedDateResponseDto(

        @Schema(description = "Id заявки", example = "123")
        Long id,

        @Schema(description = "Установленная дата", example = "example = \"2024-11-06T10:00:00\"")
        String date

) {
}
