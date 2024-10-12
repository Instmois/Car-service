package ru.unn.autorepairshop.security.dto;

public record JwtResponse(

        String email,

        String accessToken,

        String refreshToken

) {
}
