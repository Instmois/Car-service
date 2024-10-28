package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AppointmentGetAllRequestDto(

        @NotNull(message = "Page number must be not null")
        @Min(value = 0, message = "Page number can't be less than 0")
        @Schema(description = "Номер страницы", example = "0")
        Integer pageNumber,

        @NotNull(message = "Page limit must be not null")
        @Min(value = 1, message = "Page limit can't be less than 1")
        @Schema(description = "Количество элементов на странице", example = "10")
        Integer pageSize

) {
}
