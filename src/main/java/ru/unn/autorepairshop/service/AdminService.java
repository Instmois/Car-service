package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.response.UserGetAllResponseDto;

public interface AdminService {

    UserGetAllResponseDto getAllUsers();

    String grantManager(Long id);

    String revokeManager(Long id);

}
