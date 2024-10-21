package ru.unn.autorepairshop.domain.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.unn.autorepairshop.domain.dto.response.UserCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.mapper.EntityResponseMapper;

@Mapper(componentModel = "spring")
public interface UserCreateResponseDtoMapper extends EntityResponseMapper<UserCreatedResponseDto, User> {

    @Override
    @Mappings({
            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "patronymic", target = "patronymic"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "authData.email", target = "email")
    })
    UserCreatedResponseDto toDto(User entity);

}
