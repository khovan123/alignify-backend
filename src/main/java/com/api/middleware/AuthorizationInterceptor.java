package com.api.middleware;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.api.util.Helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/v1/auth/google",
            "/api/v1/auth/request-otp/**",
            "/api/v1/auth/verify-otp/**",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/recovery-password/**",
            "/api/v1/auth/reset-password/**",
            "/api/v1/role",
            "/api/v1/category");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        if (EXCLUDED_PATHS.stream().anyMatch(path -> uri.startsWith(path) || uri.matches(path.replace("/**", ".*")))) {
            return true;
        }

        if ((method.equals("PUT") || method.equals("DELETE"))) {
            String id = uri.substring(uri.lastIndexOf('/') + 1);

            if (!Helper.isOwner(id, request)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write(String.format(
                        "{\"code\":403,\"message\":\"Access denied: Insufficient permissions\",\"path\":\"%s\"}",
                        uri));
                return false;
            }
        }

        // Kiểm tra POST nếu endpoint thuộc danh sách bảo vệ
        if (method.equals("POST")) {
            String id = uri.substring(uri.lastIndexOf('/') + 1);
            for (String protectedPath : protectedPostPaths) {
                if (uri.contains(protectedPath)) {
                    if (!Helper.isOwner(id, request)) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.getWriter().write(String.format(
                                "{\"code\":403,\"message\":\"Access denied: Insufficient permissions\",\"path\":\"%s\"}",
                                uri));
                        return false;
                    }
                }
            }
        }

        // if (method.equals("POST")) {
        // String id = uri.substring(uri.lastIndexOf('/') + 1);
        // if (!Helper.isOwner(id, request)) {
        // response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // response.setContentType("application/json");
        // response.getWriter().write(String.format(
        // "{\"code\":403,\"message\":\"Access denied: Insufficient
        // permissions\",\"path\":\"%s\"}",
        // uri
        // ));
        // return false;
        // }
        // }
        return true;
    }
}
