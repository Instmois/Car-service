package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dto-response со списком всех механиков")
public record MechanicListResponseDto(

        @Schema(description = "Список механиков")
        List<MechanicResponseDto> mechanics

) {
}
