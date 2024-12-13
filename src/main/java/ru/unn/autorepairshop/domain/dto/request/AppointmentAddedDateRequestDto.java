package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dto-request на смену начала и окончания выполнения заявки")
public record AppointmentAddedDateRequestDto(

        @Schema(
                description = "Дата на обновление либо начала заявки, либо окончания",
                example = "2024-11-01T14:30:00"
        )
        @NotNull(message = "Дата записи не может быть null.")
        LocalDateTime appointmentDate

) {
}
