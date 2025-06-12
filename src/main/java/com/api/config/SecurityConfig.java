package com.api.config;

import com.api.middleware.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    @Autowired
//    private final AuthenticationConfiguration authenticationConfiguration;
    
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
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
                        "/api/v1/auth/google/**",
                        "/api/v1/auth/google",
                        "/api/v1/auth/login",
                        "/api/v1/auth/recovery-password",
                        "/api/v1/auth/reset-password/**"
                ).permitAll()
                .anyRequest().authenticated()
                ).sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
//     @Bean
//    public AuthenticationManager authenticationManager() {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

}
