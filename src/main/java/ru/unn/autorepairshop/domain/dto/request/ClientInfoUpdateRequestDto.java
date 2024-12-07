package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Форма для обновления данных о себе")
public record ClientInfoUpdateRequestDto(

        @Schema(description = "Имя", example = "Иван")
        @Length(max = 255, message = "Firstname length must be smaller than 255 symbols")
        String firstName,

        @Schema(description = "Фамилия", example = "Иванов")
        @Length(max = 255, message = "Lastname length must be smaller than 255 symbols")
        String lastName,

        @Schema(description = "Отчество", example = "Иванович")
        @Length(max = 255, message = "Patronymic length must be smaller than 255 symbols")
        String patronymic,

        @Schema(description = "Номер телефона", example = "88005553535")
        @Pattern(
                regexp = "^(8\\d{10}|\\+7\\d{10})$",
                message = "Телефонный номер должен начинаться с 8 и содержать 11 цифр или с +7 и содержать 11 цифр"
        )
        String phoneNumber,

        @Schema(description = "Email", example = "ivan@mail.ru")
        @Email(message = "Wrong email format")
        String email

) {
}
