package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.request.JwtRefreshRequestDto;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.security.dto.JwtRequest;
import ru.unn.autorepairshop.security.dto.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(JwtRefreshRequestDto refreshToken);

    JwtResponse register(UserCreateRequestDto registerRequest);

}
