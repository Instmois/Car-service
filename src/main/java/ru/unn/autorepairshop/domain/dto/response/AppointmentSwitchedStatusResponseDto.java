package ru.unn.autorepairshop.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto-response ��� ����� ������� � ������")
public record AppointmentSwitchedStatusResponseDto(

        @Schema(description = "Id ������", example = "123")
        Long appointmentId,

        @Schema(description = "������ ������", example = "DONE")
        String status

) {
}
