package ru.unn.autorepairshop.domain.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.mapper.EntityRequestMapper;

@Mapper(componentModel = "spring")
public interface UserCreateRequestDtoMapper extends EntityRequestMapper<UserCreateRequestDto, User> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authData", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    User toEntity(UserCreateRequestDto dto);

}
