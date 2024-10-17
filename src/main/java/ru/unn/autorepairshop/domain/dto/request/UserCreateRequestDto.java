package ru.unn.autorepairshop.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserCreateRequestDto(

        @NotNull(message = "Firstname must be not null")
        @Length(max = 255, message = "Firstname length must be smaller than 255 symbols")
        String firstName,

        @NotNull(message = "Lastname must be not null")
        @Length(max = 255, message = "Lastname length must be smaller than 255 symbols")
        String lastName,

        @Length(max = 255, message = "Patronymic length must be smaller than 255 symbols")
        String patronymic,

        @Pattern(regexp = "^[78]\\d{10}$", message = "Phone number must be 11 digits and start with 7 or 8")
        String phoneNumber,

        @NotNull(message = "Password must be not null")
        String password,

        @NotNull(message = "Confirmation must be not null")
        String repeatPassword,

        @NotNull(message = "Email must be not null")
        @Email(message = "Wrong email format")
        String email

) {
}
