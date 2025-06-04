package com.api.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

public class Helper {

    public static boolean isOwner(String id, HttpServletRequest request) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String ownerId = decodeJWT.getSubject();
        return ownerId.equalsIgnoreCase(id);
    }
    
    public static String extractUserIdFromRequest(HttpServletRequest request) {
    DecodedJWT jwt = JwtUtil.decodeToken(request);
    return jwt.getSubject();
    }

}
