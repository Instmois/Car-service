package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response с id заявки и мастера")
public record AppointmentAddedMechanicResponseDto(

        @Schema(description = "Id заявки", example = "123")
        Long appointmentId,

        @Schema(description = "Id мастера", example = "123")
        Long mechanicId

) {
}
