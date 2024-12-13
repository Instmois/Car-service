package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response с информацией о механике")
public record MechanicResponseDto(

        @Schema(description = "Id механика", example = "123")
        Long id,

        @Schema(description = "ФИО механика", example = "Иванов И.И.")
        String initials

) {
}
