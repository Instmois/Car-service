package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dto для получения информации о заявках с пагинацией и фильтрацией")
public class AppointmentGetAllRequestDto {

    @NotNull(message = "Номер страницы не должен быть null")
    @Min(value = 0, message = "Номер страницы не должен быть меньше чем 0")
    @Schema(description = "Номер страницы", example = "0")
    private Integer pageNumber = 0;

    @NotNull(message = "Количество элементов на странице не должно быть null")
    @Min(value = 1, message = "Количество элементов на странице не должно быть меньше 1")
    @Schema(description = "Количество элементов на странице", example = "24")
    private Integer pageSize = 24;

    @Schema(description = "Фамилия клиента, по которому идет фильтрация", example = "Иванов")
    private String client = "";

    @Schema(description = "Фильтр по статусу заявок", example = "Новая")
    @Pattern(regexp = "(?i)new|at_work|done|empty", message = "Фильтр должен быть либо new, at_work, done или empty")
    private String statusFilter = "empty";

}
