package com.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.api.config.EnvConfig;
import com.api.model.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

public class JwtUtil {

    public static DecodedJWT decodeToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("MISSING_OR_INVALID_AUTHORIZATION_HEADER");
        }

        String token = authHeader.replace("Bearer ", "");
        return JWT.require(EnvConfig.ALGORITHM)
                .build()
                .verify(token);
    }

    public static DecodedJWT decodeToken(String token) {
        return JWT.require(EnvConfig.ALGORITHM)
                .build()
                .verify(token);
    }

    public static String createToken(User user) {
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("roleId", user.getRoleId())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(EnvConfig.ALGORITHM);
    }

    public static String createResetToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(EnvConfig.ALGORITHM);
    }

    public static String createURLResetPassword(String url, String email) {
        return (url.endsWith("/") ? url : url + "/") + createResetToken(email);
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static boolean isCorrectPassword(String pw_hashed, String pw_plaintext) {
        return BCrypt.checkpw(pw_plaintext, pw_hashed);
    }

}
