package com.api.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.api.config.*;
import com.api.dto.*;
import com.api.model.*;
import com.api.repository.*;
import com.api.service.*;
import com.api.util.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InfluencerRepository influencerRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private AuthService authService;

    @GetMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String email) {
        try {
            return ResponseEntity.status(200).body(Map.of(
                    "message", authService.generateOtp(email)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Opssss! Something went wrong."
            ));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            boolean isValid = authService.verifyOtp(email, otp);
            if (isValid) {
                return ResponseEntity.status(200).body(Map.of("message", "OTP verified successfully"));
            } else {
                return ResponseEntity.status(400).body(Map.of("error", "Wrong OTP code."));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Opssss! Something went wrong."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "Email exists"
            ));
        }
        user.setPassword(JwtUtil.hashPassword(user.getPassword()));
        Optional<Role> role = roleRepository.findById(user.getRoleId());
        if (role.isPresent()) {
            user = userRepository.save(user);
            if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
                Influencer influencer = new Influencer();
                influencer.setUserId(user.getUserId());
                influencer.setCreateAt(user.getCreateAt());
                Gallery gallery = new Gallery();
                gallery.setGalleryId(user.getUserId());
                gallery.setCreateAt(user.getCreateAt());
                influencerRepository.save(influencer);
                galleryRepository.save(gallery);
            } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID)) {
                Brand brand = new Brand();
                brand.setUserId(user.getUserId());
                brand.setCreateAt(user.getCreateAt());
                brandRepository.save(brand);
            } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.ADMIN_ROLE_ID)) {
                userRepository.deleteById(user.getUserId());
                return ResponseEntity.status(403).body(Map.of(
                        "error", "Access is denied."
                ));
            }
        } else {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Invalid role."
            ));
        }

        return ResponseEntity.status(201).body(Map.of(
                "message", "Registration successful"
        ));
    }

    @PostMapping("/register-secret")
    public ResponseEntity<?> registerSecret(@RequestBody Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "Email exists"
            ));
        }
        admin.setPassword(JwtUtil.hashPassword(admin.getPassword()));
        try {
            adminRepository.save(admin);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage()
            ));
        }
        return ResponseEntity.status(201).body(Map.of(
                "message", "Registration successful"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isEmpty() || !JwtUtil.isCorrectPassword(existing.get().getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid credentials"
            ));
        }

        return ResponseEntity.status(200).body(Map.of(
                "token", JwtUtil.createToken(existing.get())));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChange passwordRequest, HttpServletRequest request) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String userId = decodeJWT.getSubject();
        User user;
        try {
            user = userRepository.findById(userId).get();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "UNAUTHORIZED"
            ));
        }

        if (!JwtUtil.isCorrectPassword(user.getPassword(), passwordRequest.getOldPassword())) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "PASSWORD_WRONG"
            ));
        }

        if (!passwordRequest.getNewPassword().equalsIgnoreCase(passwordRequest.getPasswordConfirm())) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "CONFIRM_PASSWORD_WRONG"
            ));
        }

        if (passwordRequest.getNewPassword().equalsIgnoreCase(passwordRequest.getOldPassword())) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "DOUBLE_PASSWORD"
            ));
        }

        user.setPassword(JwtUtil.hashPassword(passwordRequest.getNewPassword()));

        return ResponseEntity.status(200).body(Map.of(
                "message", "Password changed."
        ));
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<?> sendEmail(@RequestParam String email) {
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "Invalid email."
            ));
        }
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "Email is not exists."
            ));
        }
        String resetURL = JwtUtil.createURLResetPassword(email);
        String subject = "Reset your password";
        String message = "Click this url: " + resetURL + " to reset your password.";
        emailService.sendSimpleEmail(email, subject, message);
        return ResponseEntity.status(200).body(Map.of(
                "message", "Recovery email sent."
        ));
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody PasswordReset passwordReset) {
        try {
            User user;
            DecodedJWT decodeJWT = JwtUtil.decodeToken(token);
            user = userRepository.findByEmail(decodeJWT.getSubject()).get();
            if (!passwordReset.getPassword().equalsIgnoreCase(passwordReset.getPasswordConfirm())) {
                return ResponseEntity.status(400).body(Map.of(
                        "error", "Confirm password wrong."
                ));
            }
            user.setPassword(JwtUtil.hashPassword(passwordReset.getPassword()));
            userRepository.save(user);
            return ResponseEntity.status(200).body(Map.of(
                    "message", "Password reset successful."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "Invalid token."
            ));
        }
    }
}
