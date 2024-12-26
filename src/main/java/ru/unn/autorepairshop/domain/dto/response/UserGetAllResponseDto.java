package ru.unn.autorepairshop.domain.dto.response;

import java.util.List;

public record UserGetAllResponseDto(List<UserResponseDto> users) {
}
