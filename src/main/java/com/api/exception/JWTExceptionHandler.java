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
        return ApiResponse.sendError(401, "TOKEN_EXPIRED", request.getRequestURI());
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<?> handleSignatureInvalid(SignatureVerificationException ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, "INVALID_TOKEN_SIGNATURE", request.getRequestURI());
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<?> handleMalformedToken(JWTDecodeException ex, HttpServletRequest request) {
        return ApiResponse.sendError(400, "MALFORMED_TOKEN", request.getRequestURI());
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<?> handleInvalidClaim(InvalidClaimException ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, "INVALID_CLAIM_TOKEN", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex, HttpServletRequest request) {
        return ApiResponse.sendError(401, "AUTHENTICATION_FAILED", request.getRequestURI());
    }
}
