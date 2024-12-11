package ru.unn.autorepairshop.domain.mapper.appointment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;

@Mapper(componentModel = "spring")
public interface AppointmentManagerInfoResponseDtoMapper {

    @Mapping(
            target = "client",
            expression = "java(entity.getClient().getLastName() + \" \" + entity.getClient().getFirstName().charAt(0) + \". \" + (entity.getClient().getPatronymic() != null ? entity.getClient().getPatronymic().charAt(0) + \".\" : \"\"))"
    )
    @Mapping(source = "appointmentDate", target = "startDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "serviceType", target = "serviceType")
    @Mapping(source = "status", target = "status")
    AppointmentManagerInfoResponseDto toDto(Appointment entity);

}
