package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;

@Schema(description = "Информация о записи")
public record AppointmentManagerInfoResponseDto(

        @Schema(description = "Id заявки", example = "123")
        Long id,

        @Schema(description = "ФИО клиента", example = "Иванов И. И")
        String client,

        @Schema(description = "Дата и время начала записи", example = "2024-11-06T10:00:00")
        String startDate,

        @Schema(description = "Тип предоставляемой услуги", example = "REPAIR")
        ServiceType serviceType,

        @Schema(description = "Статус записи", example = "NEW")
        AppointmentStatus status

) {
}
