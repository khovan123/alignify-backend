package com.api.config;

import com.auth0.jwt.algorithms.Algorithm;

public class EnvConfig {

    public static final Algorithm ALGORITHM = Algorithm.HMAC256("secretKey");
    public static String BRAND_ROLE_ID = "6834312517f64328628a78dc";
    public static String INFLUENCER_ROLE_ID = "6834312517f64328628a78dd";
    public static String ADMIN_ROLE_ID = "6834312517f64328628a78db";
}
