package ru.unn.autorepairshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.UserCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.UserCreatedResponseDto;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.domain.mapper.user.UserCreateRequestDtoMapper;
import ru.unn.autorepairshop.domain.mapper.user.UserCreateResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.AuthException;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.repository.UserRepository;
import ru.unn.autorepairshop.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserCreateRequestDtoMapper userCreateRequestDtoMapper;

    private final UserCreateResponseDtoMapper userCreateResponseDtoMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserCreateRequestDtoMapper userCreateRequestDtoMapper,
            UserCreateResponseDtoMapper userCreateResponseDtoMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userCreateRequestDtoMapper = userCreateRequestDtoMapper;
        this.userCreateResponseDtoMapper = userCreateResponseDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_ID::get);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository
                .findByAuthData_Email(email)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_EMAIL::get);
    }

    @Override
    @Transactional
    public UserCreatedResponseDto create(UserCreateRequestDto request) {
        User user = userCreateRequestDtoMapper.toEntity(request);
        AuthData authData = new AuthData();

        if (userRepository.findByAuthData_Email(request.email()).isPresent()) {
            throw AuthException.CODE.EMAIL_IN_USE.get();
        }

        if (!request.password().equals(request.repeatPassword())) {
            throw AuthException.CODE.INVALID_REPEAT_PASSWORD.get();
        }

        authData.setPassword(passwordEncoder.encode(request.password()));
        authData.setRole(Role.CLIENT);
        authData.setEmail(request.email());
        authData.setUser(user);
        user.setAuthData(authData);

        userRepository.save(user);

        return userCreateResponseDtoMapper.toDto(user);
    }

}
