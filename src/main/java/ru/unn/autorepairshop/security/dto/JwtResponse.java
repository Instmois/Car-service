package ru.unn.autorepairshop.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {

    private String email;

    private String accessToken;

    private String refreshToken;

}
