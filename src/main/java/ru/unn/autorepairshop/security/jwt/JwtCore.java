package ru.unn.autorepairshop.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.unn.autorepairshop.domain.entity.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class JwtCore {

    private final JwtProperties jwtProperties;

    private SecretKey accessKey;

    private SecretKey refreshKey;

    @Autowired
    public JwtCore(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void init() {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getAccessSecret()));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getRefreshSecret()));
    }

    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getAuthData().getEmail());
        claims.put("id", user.getId());
        claims.put("role", user.getAuthData().getRole().toString());
        Instant validity = Instant.now().plus(jwtProperties.getAccessTime(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(validity))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getAuthData().getEmail());
        claims.put("id", user.getId());
        claims.put("role", user.getAuthData().getRole().toString());
        Instant validity = Instant.now().plus(jwtProperties.getRefreshTime(), ChronoUnit.DAYS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(validity))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken){
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken){
        return validateToken(refreshToken, refreshKey);
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, accessKey);
    }

    public boolean validateToken(String token, Key key) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("Token has expired");
        } catch (UnsupportedJwtException e) {
            log.info("Something went wrong");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token");
        } catch (SignatureException e) {
            log.info("Invalid signature");
        }

        return false;
    }

    public String getId(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id").toString();
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
