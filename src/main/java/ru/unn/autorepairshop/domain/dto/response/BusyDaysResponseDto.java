package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Список занятых дат")
public record BusyDaysResponseDto(

        @Schema(description = "Список занятых дат", example = "[\"2024-11-06T10:00:00\", \"2024-11-06T11:00:00\", \"2024-11-07T14:30:00\"]")
        List<LocalDateTime> busyDays

) {
}