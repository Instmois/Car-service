package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.unn.autorepairshop.domain.enums.ServiceType;

import java.time.LocalDateTime;

@Schema(
        description = """
                DTO-запрос для создания записи на обслуживание, \n
                содержащий данные о транспортном средстве и запрашиваемых услугах.
                """
)
public record AppointmentCreateRequestDto(

        @Schema(
                description = "Госномер транспортного средства.",
                example = "A000AA"
        )
        @NotNull(message = "Госномер не может быть null.")
        @NotEmpty(message = "Госномер не должен быть пустым.")
        @Size(min = 2, max = 10, message = "Госномер должен содержать от 2 до 10 символов.")
        String licensePlate,

        @Schema(
                description = "Модель транспортного средства.",
                example = "Toyota Camry"
        )
        @NotNull(message = "Модель не может быть null.")
        @NotEmpty(message = "Модель не должна быть пустой.")
        String model,

        @Schema(
                description = "Планируемые дата и время записи на обслуживание.",
                example = "2024-11-01T14:30:00"
        )
        @NotNull(message = "Дата записи не может быть null.")
        @Future(message = "Дата записи должна быть в будущем.")
        LocalDateTime appointmentDate,

        @Schema(
                description = "Тип услуги, которая будет выполнена.",
                example = "REPAIR"
        )
        @NotNull(message = "Тип услуги не может быть null.")
        ServiceType serviceType

) {
}
