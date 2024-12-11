package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO-запрос для обновления пароля клиента")
public record ClientUpdatePasswordRequestDto(

        @Schema(description = "Старый пароль клиента", example = "oldPassword123")
        @NotBlank(message = "Старый пароль не может быть пустым")
        String oldPassword,

        @Schema(description = "Новый пароль клиента", example = "newPassword123")
        @NotBlank(message = "Новый пароль не может быть пустым")
        String newPassword,

        @Schema(description = "Подтверждение нового пароля", example = "newPassword123")
        @NotBlank(message = "Подтверждение пароля не может быть пустым")
        String confirmPassword

) {
}
