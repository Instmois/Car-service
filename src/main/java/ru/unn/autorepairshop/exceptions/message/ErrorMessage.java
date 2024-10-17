package ru.unn.autorepairshop.exceptions.message;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Стандартное описание ошибки")
public record ErrorMessage(

        @Schema(description = "Код ошибки", example = "EXCEPTION")
        String code,

        @Schema(description = "Сообщение ошибки", example = "Неправильный запрос")
        String message

) {
}
