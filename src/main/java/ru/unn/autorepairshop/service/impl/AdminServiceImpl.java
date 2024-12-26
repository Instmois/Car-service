package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.response.UserGetAllResponseDto;
import ru.unn.autorepairshop.domain.dto.response.UserResponseDto;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.service.AdminService;
import ru.unn.autorepairshop.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserGetAllResponseDto getAllUsers() {
        List<User> users = userService.getAll();
        List<UserResponseDto> dtos = users.stream()
                .filter(e -> !e.getAuthData().getRole().equals(Role.ROLE_ADMIN))
                .map(e -> new UserResponseDto(
                        e.getId(),
                        e.getAuthData().getEmail(),
                        e.getAuthData().getRole().toString())
                )
                .toList();
        return new UserGetAllResponseDto(dtos);
    }

    @Override
    @Transactional
    public String grantManager(Long id) {
        User user = userService.getById(id);

        if (!user.getAuthData().getRole().equals(Role.ROLE_CLIENT)) {
            throw UserException.CODE.USER_IS_NOT_A_CLIENT.get();
        }

        user.getAuthData().setRole(Role.ROLE_MANAGER);

        return Role.ROLE_MANAGER.toString();
    }

    @Override
    @Transactional
    public String revokeManager(Long id) {
        User user = userService.getById(id);

        if (!user.getAuthData().getRole().equals(Role.ROLE_MANAGER)) {
            throw UserException.CODE.USER_IS_NOT_A_MANAGER.get();
        }

        user.getAuthData().setRole(Role.ROLE_CLIENT);

        return Role.ROLE_CLIENT.toString();
    }

}
