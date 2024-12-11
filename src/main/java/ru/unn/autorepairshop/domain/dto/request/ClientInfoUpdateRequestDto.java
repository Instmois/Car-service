package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Форма для обновления данных о себе")
public record ClientInfoUpdateRequestDto(

        @Schema(description = "Имя", example = "Иван")
        @Length(max = 255, message = "Длина имени должна быть меньше 255 символов")
        String firstName,

        @Schema(description = "Фамилия", example = "Иванов")
        @Length(max = 255, message = "Длина фамилии должна быть меньше 255 символов")
        String lastName,

        @Schema(description = "Отчество", example = "Иванович")
        @Length(max = 255, message = "Длина отчества должна быть меньше 255 символов")
        String patronymic,

        @Schema(description = "Номер телефона", example = "88005553535")
        @Pattern(
                regexp = "^(8\\d{10}|\\+7\\d{10})$",
                message = "Телефонный номер должен начинаться с 8 и содержать 11 цифр или с +7 и содержать 11 цифр"
        )
        String phoneNumber,

        @Schema(description = "Email", example = "ivan@mail.ru")
        @Email(message = "Неверный формат электронной почты")
        String email

) {
}
