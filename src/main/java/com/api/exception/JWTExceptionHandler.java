package com.api.exception;

import com.auth0.jwt.exceptions.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
public class JWTExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpired(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "TOKEN_EXPIRED"));
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<?> handleSignatureInvalid(SignatureVerificationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "INVALID_TOKEN_SIGNATURE"));
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<?> handleMalformedToken(JWTDecodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "MALFORMED_TOKEN"));
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<?> handleInvalidClaim(InvalidClaimException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "INVALID_CLAIM_TOKEN"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "AUTHENTICATION_FAILED", "error", ex.getMessage()));
    }
}
