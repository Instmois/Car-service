package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление токена")
public record JwtRefreshRequestDto(

        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbiIsImlkIjoxLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImlhdCI6MTcwODA3MzYxOSwiZXhwIjoxNzA4MDc3MjE5fQ.LoAeZKoTYeC8R5obPehniC5EdTCTkbvikKyJ0DBi9hbY35NLa-Ucb7uVFJtB7VDoAIWEypMTZd62B9Ub6A82TQ")
        String refreshToken

) {
}
