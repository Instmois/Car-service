package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dto-request на заказ детали")
public record PartOrderCreateRequestDto(

        @Schema(description = "Тип детали", example = "ENGINE_OIL")
        @NotNull(message = "Тип детали не может быть null.")
        String partName,

        @Schema(description = "Количество", example = "1")
        @Min(value = 1, message = "Количество не может быть меньше 1")
        @NotNull(message = "Количество не может быть null.")
        Integer amount,

        @Schema(
                description = "Планируемая дата доставки",
                example = "2024-12-31"
        )
        @NotNull(message = "Дата доставки не может быть null.")
        @Future(message = "Дата доставки должна быть в будущем.")
        LocalDateTime appointmentDate,

        @Schema(description = "Общая стоимость", example = "199.49")
        @Min(value = 1, message = "Стоимость не может быть меньше 1")
        @NotNull(message = "Стоимость не может быть null.")
        BigDecimal price

) {
}
