package com.api.controller;

import com.api.dto.*;
import com.api.model.*;
import com.api.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam("email") String email) {
        return authService.sendOtpCode(email);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        return authService.verifyOtpCode(email, otp);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return authService.registerAccount(user);
    }

    @PostMapping("/register-secret")
    public ResponseEntity<?> registerSecret(@RequestBody Admin admin) {
        return authService.registerAdmin(admin);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return authService.loginAccount(user);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChange passwordRequest, HttpServletRequest request) {
        return authService.changeUserPassword(passwordRequest, request);
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> recoveryPassword(@RequestParam("email") String email) {
        return authService.recoveryPasswordByEndpoint(email);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody PasswordReset passwordReset) {
        return authService.recoveryPasswordByEndpoint(token);
    }
}
