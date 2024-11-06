package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;

@Schema(description = "Информация о записи")
public record AppointmentResponseDto(

        @Schema(description = "Дата и время начала записи", example = "2024-11-06T10:00:00")
        String startDate,

        @Schema(description = "Дата и время окончания записи", example = "2024-11-06T10:00:00")
        String endDate,

        @Schema(description = "Тип предоставляемой услуги", example = "REPAIR")
        ServiceType serviceType,

        @Schema(description = "Модель автомобиля", example = "Toyota Camry")
        String carModel,

        @Schema(description = "Номерной знак автомобиля", example = "A000AA")
        String licensePlate,

        @Schema(description = "Имя мастера", example = "Иванов И.И.")
        String master,

        @Schema(description = "Статус записи", example = "NEW")
        AppointmentStatus status

) {
}

