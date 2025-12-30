package com.qadri.tada.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionConfig -> {
                    exceptionConfig.accessDeniedHandler(((request, response, accessDeniedException) -> {
                        log.error("error occurred {}", accessDeniedException.getLocalizedMessage());
                        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
                    }));
                });

        return httpSecurity.build();
    }
}
