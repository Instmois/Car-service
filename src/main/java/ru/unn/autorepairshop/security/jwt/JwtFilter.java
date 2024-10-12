package ru.unn.autorepairshop.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.unn.autorepairshop.security.service.UserDetailsServiceImpl;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private static final String START_WITH = "Bearer ";

    private final JwtCore jwtCore;

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtFilter(JwtCore jwtCore, UserDetailsServiceImpl userDetailsService) {
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = extractJwt(request);

        if (jwt != null && jwtCore.validateAccessToken(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            Claims claims = jwtCore.getAccessClaims(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            ));
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwt(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearer) && bearer.startsWith(START_WITH)) {
            return bearer.substring(7);
        }

        return null;
    }
}
