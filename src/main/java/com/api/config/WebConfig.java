package com.api.config;

import org.springframework.context.annotation.Configuration;
import com.api.middleware.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    @Value("${cors.allowed-origins}")
    private String origins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authorizationInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/v3/api-docs",
//                        "/v3/api-docs/**",
//                        "/swagger-ui",
//                        "/swagger-ui/**",
//                        "/swagger-ui.html",
//                        "/swagger-ui.html/**",
//                        "/api/v1/roles",
//                        "/api/v1/categories",
//                        "/api/v1/auth/request-otp/**",
//                        "/api/v1/auth/verify-otp/**",
//                        "/api/v1/auth/register/**",
//                        "/api/v1/auth/google/**",
//                        "/api/v1/auth/google",
//                        "/api/v1/auth/login",
//                        "/api/v1/auth/recovery-password",
//                        "/api/v1/auth/reset-password/**",
//                        "/ws/**"
//                );
//    }
}
