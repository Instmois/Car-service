package ru.unn.autorepairshop.security.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {

    @Value("${security.jwt.access.secret}")
    private String accessSecret;

    @Value("${security.jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${security.jwt.access.time}")
    private Long accessTime;

    @Value("${security.jwt.refresh.time}")
    private Long refreshTime;

}
