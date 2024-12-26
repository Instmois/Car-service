package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.UserCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    UserCreatedResponseDto create(UserCreateRequestDto request);

    List<User> getAllByVehicleLicencePlate(String licensePlate);

    User save(User user);

    Optional<User> getOptionalByEmail(String email);

    List<User> getAll();

}
