package ru.unn.autorepairshop.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.unn.autorepairshop.domain.enums.ServiceType;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Request DTO for creating an appointment, containing vehicle details and requested services.")
public record AppointmentCreateRequestDto(

        @Schema(
                description = "License plate of the vehicle.",
                example = "A000AA"
        )
        @NotNull(message = "License plate cannot be null.")
        @NotEmpty(message = "License plate must not be empty.")
        @Size(min = 2, max = 10, message = "License plate must be between 2 and 10 characters.")
        String licensePlate,

        @Schema(
                description = "Model of the vehicle.",
                example = "Toyota Camry"
        )
        @NotNull(message = "Model cannot be null.")
        @NotEmpty(message = "Model must not be empty.")
        String model,

        @Schema(
                description = "Planned date and time for the appointment.",
                example = "2024-11-01T14:30:00"
        )
        @NotNull(message = "Appointment date cannot be null.")
        @Future(message = "Appointment date must be in the future.")
        LocalDateTime appointmentDate,

        //todo переписать example
        @Schema(
                description = "List of service types to be performed.",
                example = "[\"OIL_CHANGE\", \"BRAKE_INSPECTION\"]"
        )
        @NotNull(message = "Service types cannot be null.")
        @Size(min = 1, message = "At least one service type must be specified.")
        List<ServiceType> serviceTypes

) {
}

