package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.UserCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.User;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    UserCreatedResponseDto create(UserCreateRequestDto request);

}
