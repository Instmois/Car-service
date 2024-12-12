package ru.unn.autorepairshop.domain.mapper.mechanic;

import org.mapstruct.Mapper;
import ru.unn.autorepairshop.domain.dto.response.MechanicResponseDto;
import ru.unn.autorepairshop.domain.entity.Mechanic;
import ru.unn.autorepairshop.domain.mapper.EntityResponseMapper;

@Mapper(componentModel = "spring")
public interface MechanicResponseDtoMapper extends EntityResponseMapper<MechanicResponseDto, Mechanic> {
}
