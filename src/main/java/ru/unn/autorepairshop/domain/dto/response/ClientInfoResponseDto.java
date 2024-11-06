package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о текущем пользователе")
public record ClientInfoResponseDto(

        @Schema(description = "Имя", example = "Иван")
        String firstName,

        @Schema(description = "Фамилия", example = "Иванов")
        String lastName,

        @Schema(description = "Отчество", example = "Иванович")
        String patronymic,

        @Schema(description = "Номер телефона", example = "88005553535")
        String phoneNumber,

        @Schema(description = "Email", example = "ivan@mail.ru")
        String email

) {
}
