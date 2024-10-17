package ru.unn.autorepairshop.domain.dto.response;

public record UserCreatedResponseDto(

        String firstName,

        String lastName,

        String patronymic,

        String phoneNumber,

        String email

) {
}
