package ru.unn.autorepairshop.domain.mapper.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.mapper.EntityResponseMapper;

@Mapper(componentModel = "spring")
public interface ClientInfoResponseDtoMapper extends EntityResponseMapper<ClientInfoResponseDto, User> {

    @Override
    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "patronymic", target = "patronymic"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "authData.email", target = "email")
    })
    ClientInfoResponseDto toDto(User entity);

}
