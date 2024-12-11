package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Форма для регистрации")
public record UserCreateRequestDto(

        @NotNull(message = "Имя не может быть null")
        @Length(max = 255, message = "Длина имени должна быть меньше 255 символов")
        @Schema(description = "Имя", example = "Иван")
        String firstName,

        @NotNull(message = "Фамилия не может быть null")
        @Length(max = 255, message = "Длина фамилии должна быть меньше 255 символов")
        @Schema(description = "Фамилия", example = "Иванов")
        String lastName,

        @Schema(description = "Отчество", example = "Иванович")
        @Length(max = 255, message = "Длина отчества должна быть меньше 255 символов")
        String patronymic,

        @Pattern(regexp = "^[78]\\d{10}$", message = "Номер телефона должен содержать 11 цифр и начинаться с 7 или 8")
        @Schema(description = "Номер телефона", example = "88005553535")
        String phoneNumber,

        @NotNull(message = "Пароль не может быть null")
        @Schema(description = "Пароль", example = "1234")
        String password,

        @NotNull(message = "Подтверждение пароля не может быть null")
        @Schema(description = "Повтор пароля", example = "1234")
        String repeatPassword,

        @NotNull(message = "Email не может быть null")
        @Email(message = "Неверный формат электронной почты")
        @Schema(description = "Email", example = "example@example.com")
        String email

) {
}
