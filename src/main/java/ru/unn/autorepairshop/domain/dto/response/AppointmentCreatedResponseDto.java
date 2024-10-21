package ru.unn.autorepairshop.domain.dto.response;

import ru.unn.autorepairshop.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentCreatedResponseDto(

        String licencePlate,

        String model,

        AppointmentStatus status,

        LocalDateTime appointmentDate

) {
}
