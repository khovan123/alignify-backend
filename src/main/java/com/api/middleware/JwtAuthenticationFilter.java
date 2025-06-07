package com.api.middleware;

import com.api.security.CustomUserDetails;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI().split("\\?")[0];
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/v3/api-docs/**")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-ui/**")
                || path.equals("/swagger-ui.html")
                || path.equals("/swagger-ui.html/**")
                || path.equals("/api/v1/role")
                || path.equals("/api/v1/category")
                || path.equals("/api/v1/auth/request-otp/**")
                || path.equals("/api/v1/auth/verify-otp/**")
                || path.equals("/api/v1/auth/register/**")
                || path.equals("/api/v1/auth/login")
                || path.equals("/api/v1/auth/google/**")
                || path.equals("/api/v1/auth/google")
                || path.matches("/api/v1/(role|category|auth/(request-otp|verify-otp|register|login|google))(.*)?")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token is not provided or incorrectly formatted");
        }

        try {
            com.auth0.jwt.interfaces.DecodedJWT decodedJWT = JwtUtil.decodeToken(request);
            String userId = decodedJWT.getSubject();
            String roleId = decodedJWT.getClaim("roleId").asString();
            CustomUserDetails userDetails = new CustomUserDetails(userId, roleId, "", "", Collections.emptyList());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        }
    }
}
