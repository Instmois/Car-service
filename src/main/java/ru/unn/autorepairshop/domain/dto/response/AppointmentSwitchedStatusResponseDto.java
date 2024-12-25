package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response дл€ смены статуса у за€вки")
public record AppointmentSwitchedStatusResponseDto(

        @Schema(description = "Id за€вки", example = "123")
        Long appointmentId,

        @Schema(description = "—татус за€вки", example = "DONE")
        String status

) {
}
