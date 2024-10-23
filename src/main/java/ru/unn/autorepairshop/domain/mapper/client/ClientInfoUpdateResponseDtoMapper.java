package ru.unn.autorepairshop.domain.mapper.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.mapper.EntityResponseMapper;

@Mapper(componentModel = "spring")
public interface ClientInfoUpdateResponseDtoMapper extends EntityResponseMapper<ClientInfoUpdateResponseDto, User> {

    @Override
    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "patronymic", target = "patronymic"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "authData.email", target = "email")
    })
    ClientInfoUpdateResponseDto toDto(User entity);

}
