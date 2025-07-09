package com.api.middleware;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.repository.RoleRepository;
import com.api.security.CustomUserDetails;
import com.api.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI().split("\\?")[0];

        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod()) || path.startsWith("/ws")) {
            String origin = request.getHeader("Origin");
            if ("https://alignify-rose.vercel.app".equals(origin) || "http://localhost:3000".equals(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
            } else {
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            // response.setHeader("Access-Control-Allow-Origin", "*");
            // response.setHeader("Access-Control-Allow-Credentials", "true");
            // response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,
            // OPTIONS");
            // response.setHeader("Access-Control-Allow-Headers", "*");
            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/v3/api-docs/**")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-ui/**")
                || path.equals("/swagger-ui.html")
                || path.equals("/swagger-ui.html/**")
                || path.equals("/api/v1/roles")
                || path.equals("/api/v1/categories")
                || path.equals("/api/v1/auth/request-otp/**")
                || path.equals("/api/v1/auth/verify-otp/**")
                || path.equals("/api/v1/auth/register/**")
                || path.equals("/api/v1/auth/register-secret/**")
                || path.equals("/api/v1/auth/register-secret")
                || path.equals("/api/v1/auth/login")
                || path.equals("/api/v1/auth/google/**")
                || path.equals("/api/v1/auth/google")
                || path.equals("/api/v1/auth/recovery-password")
                || path.equals("/api/v1/auth/reset-password/**")
                || path.matches(
                        "/api/v1/(roles|categories|auth/(request-otp|verify-otp|register|register-secret|login|google|reset-password|recovery-password))(.*)?")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token is not provided or incorrectly formatted");
        }

        com.auth0.jwt.interfaces.DecodedJWT decodedJWT = JwtUtil.decodeToken(request);
        if (decodedJWT == null) {
            this.sendErrorResponse(response, path, "Invalid token: Token is missing or malformed");
        }
        String userId = decodedJWT.getSubject();
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String roleId = decodedJWT.getClaim("roleId").asString();
        if (roleId == null || !roleRepository.findById(roleId).isPresent()) {
            this.sendErrorResponse(response, path, "Invalid token: Role ID is missing");
            return;
        }
        CustomUserDetails userDetails = new CustomUserDetails(userId, roleId, "", "");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String path, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\",\"path\":\"" + path + "\"}");
    }
}
