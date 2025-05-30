package com.api.service;

import com.api.config.EnvConfig;
import com.api.dto.*;
import com.api.model.*;
import com.api.repository.*;
import com.api.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class AuthService {

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
    private EmailService emailSerivce;
    @Autowired
    private OtpService otpService;

    public ResponseEntity<?> loginViaGoogle(String authCode, HttpServletRequest request) {
        if (request.getHeader("X-Requested-With") == null) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", "Missing headers"
            ));
        }
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    "136808391187-o5vdain002hlpj62998pfs58ko3qa0sh.apps.googleusercontent.com",
                    "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",
                    authCode,
                    "postmessage"
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user;
            if (!userRepository.existsByEmail(email)) {
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setRoleId(EnvConfig.INFLUENCER_ROLE_ID);
                user.setPassword(JwtUtil.hashPassword(email));
                user = userRepository.save(user);
            } else {
                user = userRepository.findByEmail(email).get();
            }

            return ResponseEntity.ok(Map.of(
                    "token", JwtUtil.createToken(user),
                    "id", user.getUserId()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    public ResponseEntity<?> sendOtpCode(String email) {
        try {
            emailSerivce.sendSimpleEmail(email, "Your OTP Code", "Your OTP code is: " + otpService.generateOtp(email) + ". It is valid for 3 minutes.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", e.getMessage()
            ));
        }
        return ResponseEntity.status(201).body(Map.of());
    }

    public ResponseEntity<?> verifyOtpCode(String email, String otp) {
        try {
            boolean isValid = otpService.verifyOtp(email, otp);
            if (isValid) {
                return ResponseEntity.status(200).body(Map.of("message", "OTP verified successfully"));
            } else {
                return ResponseEntity.status(400).body(Map.of("error", "Wrong OTP code."));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    public ResponseEntity<?> registerAccount(User user) {
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

    public ResponseEntity<?> registerAdmin(Admin admin) {
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

    public ResponseEntity<?> loginAccount(User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (!existing.isPresent() || !JwtUtil.isCorrectPassword(existing.get().getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid credentials"
            ));
        }
        return ResponseEntity.status(200).body(Map.of(
                "token", JwtUtil.createToken(existing.get()),
                "id", existing.get().getUserId()
        ));
    }

    public ResponseEntity<?> changeUserPassword(PasswordChange passwordRequest, HttpServletRequest request) {
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

    public ResponseEntity<?> recoveryPasswordByEndpoint(String email) {
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

    public ResponseEntity<?> resetPasswordByToken(String token, PasswordReset passwordReset) {
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
