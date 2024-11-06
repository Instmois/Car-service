package ru.unn.autorepairshop.domain.dto.response;

import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;

public record AppointmentResponseDto(

        String startDate,

        String endDate,

        ServiceType serviceType,

        String carModel,

        String licensePlate,

        String master,

        AppointmentStatus status

) {
}
