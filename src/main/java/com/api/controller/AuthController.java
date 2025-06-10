package com.api.controller;

import com.api.dto.request.VerifyOTPRequest;
import com.api.dto.request.RegisterRequest;
import com.api.dto.request.RecoveryPasswordRequest;
import com.api.dto.request.PasswordResetRequest;
import com.api.dto.request.PasswordChangeRequest;
import com.api.dto.request.LoginRequest;
import com.api.dto.*;
import com.api.model.*;
import com.api.security.CustomUserDetails;
import com.api.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestParam("code") String authCode, HttpServletRequest request) {
        return authService.loginViaGoogle(authCode, request);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam("email") String email, HttpServletRequest request) {
        return authService.sendRequestOtpCode(email, request);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOTPRequest verifyOTPRequest, HttpServletRequest request) {
        return authService.verifyOtpCode(verifyOTPRequest, request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, @RequestParam("roleId") String roleId, HttpServletRequest request) {
        return authService.registerAccount(registerRequest, roleId, request);
    }

    @PostMapping("/register-secret")
    public ResponseEntity<?> registerSecret(@RequestBody Admin admin, HttpServletRequest request) {
        return authService.registerAdmin(admin, request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return authService.loginAccount(loginRequest, request);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordRequest, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return authService.changeUserPassword(passwordRequest, userDetails, request);
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestBody RecoveryPasswordRequest recoveryPasswordRequest, HttpServletRequest request) {
        return authService.recoveryPasswordByEndpoint(recoveryPasswordRequest, request);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody PasswordResetRequest passwordReset, HttpServletRequest request) {
        return authService.resetPasswordByToken(token, passwordReset, request);
    }
}
