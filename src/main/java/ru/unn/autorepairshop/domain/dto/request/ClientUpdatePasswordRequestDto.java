package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request DTO for updating client's password")
public record ClientUpdatePasswordRequestDto(

        @Schema(description = "Old password of the client", example = "oldPassword123")
        @NotBlank(message = "Old password cannot be blank")
        String oldPassword,

        @Schema(description = "New password of the client", example = "newPassword123")
        @NotBlank(message = "New password cannot be blank")
        String newPassword,

        @Schema(description = "Confirmation of the new password", example = "newPassword123")
        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword

) {
}