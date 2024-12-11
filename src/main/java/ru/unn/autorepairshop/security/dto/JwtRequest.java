package ru.unn.autorepairshop.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Email и пароль")
@AllArgsConstructor
public class JwtRequest {

    @Schema(description = "email пользователя", example = "test@test.com")
    @NotNull(message = "email не должен быть null")
    private String email;

    @Schema(description = "пароль", example = "1234")
    @NotNull(message = "пароль не должен быть null")
    private String password;

}