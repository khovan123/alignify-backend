package com.api.config;

import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @Value("${spring.api.secret-key}")
    private String secretKey;

    public static Algorithm ALGORITHM;

    public static String BRAND_ROLE_ID = "68485dcedda6867ca0d23e8a";
    public static String INFLUENCER_ROLE_ID = "68485dcedda6867ca0d23e8b";
    public static String ADMIN_ROLE_ID = "68485dcedda6867ca0d23e89";

    @PostConstruct
    public void init() {
        ALGORITHM = Algorithm.HMAC256(secretKey);
    }
}
