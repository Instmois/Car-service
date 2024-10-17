package ru.unn.autorepairshop.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "AccessToken и RefreshToken + email")
public class JwtResponse {

    @Schema(description = "email", example = "example@example.com")
    private String email;

    @Schema(description = "access token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbiIsImlkIjoxLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImlhdCI6MTcwODA3MzYxOSwiZXhwIjoxNzA4MDc3MjE5fQ.LoAeZKoTYeC8R5obPehniC5EdTCTkbvikKyJ0DBi9hbY35NLa-Ucb7uVFJtB7VDoAIWEypMTZd62B9Ub6A82TQ")
    private String accessToken;

    @Schema(description = "refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbiIsImlkIjoxLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImlhdCI6MTcwODA3MzYxOSwiZXhwIjoxNzA4MDc3MjE5fQ.LoAeZKoTYeC8R5obPehniC5EdTCTkbvikKyJ0DBi9hbY35NLa-Ucb7uVFJtB7VDoAIWEypMTZd62B9Ub6A82TQ")
    private String refreshToken;

}
