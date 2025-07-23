package com.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.middleware.JwtAuthenticationFilter;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui.html/**",
                                "/api/v1/roles",
                                "/api/v1/categories",
                                "/api/v1/auth/request-otp/**",
                                "/api/v1/auth/verify-otp/**",
                                "/api/v1/auth/register/**",
                                "/api/v1/auth/register-secret/**",
                                "/api/v1/auth/google/**",
                                "/api/v1/auth/google",
                                "/api/v1/auth/login",
                                "/api/v1/auth/recovery-password",
                                "/api/v1/auth/reset-password/**",
                                "/ws/**",
                                "/error/**",
                                "/success/**",
                                "/favicon.ico/**",
                                "/cancel/**",
                                "/custom/error/**",
                                "/api/v1/error/**",
                                "/api/v1/success/**",
                                "/api/v1/favicon.ico/**",
                                "/api/v1/cancel/**",
                                "/api/v1/custom/error/**",
                                "/api/v1/plans/brand/**",
                                "/api/v1/plans/influencer/**",
                                "/api/v1/payment/payos_transfer_handler/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}