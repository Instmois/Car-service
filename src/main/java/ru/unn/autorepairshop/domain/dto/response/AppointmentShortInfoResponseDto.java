package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;

@Schema(description = "Dto-response с информацией о заявке для окна заявки менеджера")
public record AppointmentShortInfoResponseDto(

        @Schema(description = "Название типа услуги", example = "REPAIR")
        ServiceType serviceType,

        @Schema(description = "ФИО мастера", example = "Сидоров А.А.")
        String master,

        @Schema(description = "Дата и время начала записи", example = "2024-11-06T10:00:00")
        String startDate,

        @Schema(description = "Дата и время окончания записи", example = "2024-11-06T10:00:00")
        String endDate,

        @Schema(description = "Статус записи", example = "NEW")
        AppointmentStatus status


) {
}
