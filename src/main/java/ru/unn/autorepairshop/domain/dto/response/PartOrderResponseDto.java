package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.unn.autorepairshop.domain.enums.PartName;
import ru.unn.autorepairshop.domain.enums.PartOrderStatus;

import java.math.BigDecimal;

@Schema(description = "DTO ответа для заказа на запчасть")
public record PartOrderResponseDto(

        @Schema(description = "Id запчасти", example = "123")
        Long id,

        @Schema(description = "Название запчасти", example = "ENGINE_OIL")
        PartName partName,

        @Schema(description = "Количество заказанных запчастей", example = "1")
        Integer amount,

        @Schema(description = "Дата заказа в формате ISO 8601", example = "2023-11-08T10:00:00Z")
        String orderDate,

        @Schema(description = "Ожидаемая дата доставки в формате ISO 8601", example = "2023-11-15T10:00:00Z")
        String deliveryDate,

        @Schema(description = "Цена за заказ", example = "199.99")
        BigDecimal price,

        @Schema(description = "Текущий статус заказа на запчасть", example = "NEED_TO_ORDER")
        PartOrderStatus partOrderStatus

) {
}
