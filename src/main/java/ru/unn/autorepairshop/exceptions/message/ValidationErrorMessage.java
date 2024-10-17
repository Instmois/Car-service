package ru.unn.autorepairshop.exceptions.message;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Описание ошибки валидации входных данных")
public record ValidationErrorMessage(

        @Schema(description = "Код ошибки", example = "EXCEPTION")
        String code,

        @Schema(description = "Сообщение ошибки", example = "Неправильный запрос")
        String message,

        @Schema(description = "Сообщения над полями, где ошибки валидации", example = """
                {
                    "sortDirection": "sortDirection must be one of: asc, desc"
                  }""")
        Map<String, String> errors

) {
}