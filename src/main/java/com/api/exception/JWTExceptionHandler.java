package com.api.exception;

import com.api.dto.ApiResponse;
import com.auth0.jwt.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class JWTExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        return ApiResponse.sendError(500, ex.getMessage(), request.getRequestURI());
    }
}
