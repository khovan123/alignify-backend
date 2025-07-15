package com.api.exception;

import com.api.dto.ApiResponse;
import com.auth0.jwt.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * JWT Exception Handler for API requests only.
 * Browser requests expecting HTML will be handled by Spring Boot's default error handling.
 */
@RestControllerAdvice(basePackages = "com.api.controller")
public class JWTExceptionHandler {

    /**
     * Check if the request expects JSON response based on Accept header
     * or if it's an API request (starts with /api)
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        String requestURI = request.getRequestURI();
        
        // Check if it's an API request path
        if (requestURI != null && requestURI.startsWith("/api")) {
            return true;
        }
        
        // Check if client accepts JSON but not HTML (prioritize JSON over HTML)
        if (acceptHeader != null) {
            return acceptHeader.contains("application/json") && 
                   !acceptHeader.contains("text/html");
        }
        
        return false;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpired(TokenExpiredException ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<?> handleSignatureInvalid(SignatureVerificationException ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<?> handleMalformedToken(JWTDecodeException ex, HttpServletRequest request) {
        return ApiResponse.sendError(400, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<?> handleInvalidClaim(InvalidClaimException ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleMissingToken(IllegalArgumentException ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleMessageConversionException(HttpMessageConversionException ex, HttpServletRequest request) {
        return ApiResponse.sendError(400, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ApiResponse.sendError(400, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception ex, HttpServletRequest request) {
        return ApiResponse.sendError(403, "Access Denied: " + ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        return ApiResponse.sendError(500, ex.getMessage(), request.getRequestURI());
    }
}
