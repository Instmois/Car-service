package ru.unn.autorepairshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.unn.autorepairshop.security.jwt.JwtAuthEntryPoint;
import ru.unn.autorepairshop.security.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private final JwtFilter jwtFilter;

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        configurer ->
                                configurer.requestMatchers(WHITE_LIST_URL).permitAll()
                                        .anyRequest().authenticated()
                )
                .exceptionHandling(
                        configurer -> configurer
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
