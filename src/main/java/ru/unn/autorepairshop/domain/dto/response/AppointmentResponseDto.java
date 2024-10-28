package ru.unn.autorepairshop.domain.dto.response;

import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;

import java.time.LocalDateTime;

public record AppointmentResponseDto(

        LocalDateTime startDate,

        LocalDateTime endDate,

        ServiceType serviceType,

        String carModel,

        String licensePlate,

        String master,

        AppointmentStatus status

) {
}
