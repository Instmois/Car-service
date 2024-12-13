package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dto-response со списком заказанных деталей для окна заявки менеджера")
public record PartOrderListResponseDto(

        @Schema(description = "Список заказанных запчастей")
        List<PartOrderResponseDto> partOrders

) {
}
