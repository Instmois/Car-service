package ru.unn.autorepairshop.security.dto;

public record JwtRequest(

        String email,

        String password

) {
}
