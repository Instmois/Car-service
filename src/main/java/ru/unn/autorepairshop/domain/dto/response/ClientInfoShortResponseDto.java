package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response с информацией о клиенте для окна заявки менеджера")
public record ClientInfoShortResponseDto(

        @Schema(description = "ФИО клиента", example = "Белов Б. В.")
        String client,

        @Schema(description = "Марка и модель авто", example = "Toyota Avensis")
        String vehicle,

        @Schema(description = "Номер телефона", example = "+79876543210")
        String phoneNumber,

        @Schema(description = "Номер авто", example = "K001KA33")
        String licensePlate

) {
}
