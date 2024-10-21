package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

@Schema(description = "Ответ о создании заявки на сервисные работы")
public record AppointmentCreatedResponseDto(

        @Schema(description = "Автомобильный номер", example = "A000AA")
        String licencePlate,

        @Schema(description = "Модель машины", example = "Toyota Camry")
        String model,

        @Schema(description = "Статус заявки", example = "New")
        AppointmentStatus status,

        @Schema(description = "Время формировании заявки")
        LocalDateTime appointmentDate

) {
}
