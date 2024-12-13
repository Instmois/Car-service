package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response с полной информацией о заявке для менеджера")
public record ManagerViewResponseDto(

        AppointmentShortInfoResponseDto appointmentShortInfoResponseDto,

        ClientInfoShortResponseDto clientInfoShortResponseDto,

        PartOrderListResponseDto partOrderResponseDto

) {
}
