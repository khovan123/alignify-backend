package com.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String origins;

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://alignify-rose.vercel.app",
                        origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    // registry.addInterceptor(authorizationInterceptor)
    // .addPathPatterns("/**")
    // .excludePathPatterns(
    // "/v3/api-docs",
    // "/v3/api-docs/**",
    // "/swagger-ui",
    // "/swagger-ui/**",
    // "/swagger-ui.html",
    // "/swagger-ui.html/**",
    // "/api/v1/roles",
    // "/api/v1/categories",
    // "/api/v1/auth/request-otp/**",
    // "/api/v1/auth/verify-otp/**",
    // "/api/v1/auth/register/**",
    // "/api/v1/auth/google/**",
    // "/api/v1/auth/google",
    // "/api/v1/auth/login",
    // "/api/v1/auth/recovery-password",
    // "/api/v1/auth/reset-password/**",
    // "/ws/**"
    // );
    // }
}
