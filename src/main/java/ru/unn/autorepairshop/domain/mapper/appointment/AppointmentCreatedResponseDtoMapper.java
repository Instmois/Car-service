package ru.unn.autorepairshop.domain.mapper.appointment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.mapper.EntityResponseMapper;

@Mapper(componentModel = "spring")
public interface AppointmentCreatedResponseDtoMapper extends EntityResponseMapper<AppointmentCreatedResponseDto, Appointment> {

    @Override
    @Mappings({
            @Mapping(source = "vehicle.licensePlate", target = "licencePlate"),
            @Mapping(source = "vehicle.model", target = "model"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "appointmentDate", target = "appointmentDate"),
            @Mapping(source = "serviceType", target = "serviceType")
    })
    AppointmentCreatedResponseDto toDto(Appointment entity);

}
