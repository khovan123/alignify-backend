package com.api.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.PasswordChange;
import com.api.dto.PasswordReset;
import com.api.model.AccountVerified;
import com.api.model.Admin;
import com.api.model.Brand;
import com.api.model.Gallery;
import com.api.model.Influencer;
import com.api.model.Role;
import com.api.model.User;
import com.api.repository.AccountVerifiedRepository;
import com.api.repository.AdminRepository;
import com.api.repository.BrandRepository;
import com.api.repository.GalleryRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.RoleRepository;
import com.api.repository.UserRepository;
import com.api.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import jakarta.servlet.http.HttpServletRequest;

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
    private OtpService otpService;
    @Autowired
    private AccountVerifiedRepository accountVerifiedRepository;
    @Value("${spring.google.client-id}")
    private String clientId;
    @Value("${spring.google.secret-key}")
    private String secretId;
    @Value("${spring.google.google-apis}")
    private String googleapis;
    @Value("${spring.google.type}")
    private String googletype;
    
    public ResponseEntity<?> loginViaGoogle(String authCode, HttpServletRequest request) {
        if (request.getHeader("X-Requested-With") == null) {
            return ApiResponse.sendError(400, "Missing required header field: X-Requested-With",
                    request.getRequestURI());
        }
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    googleapis,
                    clientId,
                    secretId,
                    authCode,
                    googletype
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
            return ApiResponse.sendSuccess(200, "Login successful",
                    Map.of(
                            "token", JwtUtil.createToken(user),
                            "id", user.getUserId()),
                    request.getRequestURI());
        } catch (IOException e) {
            return ApiResponse.sendError(401, "Invalid Google authentication code", request.getRequestURI());
        }
    }
    
    public ResponseEntity<?> sendOtpCode(String email, HttpServletRequest request) {
        try {
//            emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP code is: " + otpService.generateOtp(email) + ". It is valid for 3 minutes.");
            emailService.sendOtpEmail(email, otpService.generateOtp(email));
        } catch (RuntimeException e) {
            return ApiResponse.sendError(400, e.getMessage(), request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "OTP sent successfully to your email", null, request.getRequestURI());
    }
    
    public ResponseEntity<?> verifyOtpCode(String email, String otp, HttpServletRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(email, otp);
            if (isValid) {
                accountVerifiedRepository.save(new AccountVerified(email));
                return ApiResponse.sendSuccess(200, "OTP verified successfully", null, request.getRequestURI());
            } else {
                return ApiResponse.sendError(401, "Invalid OTP code", request.getRequestURI());
            }
        } catch (RuntimeException e) {
            return ApiResponse.sendError(400, e.getMessage(), request.getRequestURI());
        }
    }
    
    public ResponseEntity<?> registerAccount(User user, HttpServletRequest request) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ApiResponse.sendError(400, "Email is existed", request.getRequestURI());
        }
        if (!accountVerifiedRepository.existsByEmail(user.getEmail())) {
            return ApiResponse.sendError(403, "Email verification required to complete registration",
                    request.getRequestURI());
        }
        user.setPassword(JwtUtil.hashPassword(user.getPassword()));
        Optional<Role> role = roleRepository.findById(user.getRoleId());
        if (role.isPresent()) {
            user = userRepository.save(user);
            if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
                Influencer influencer = new Influencer();
                influencer.setUserId(user.getUserId());
                influencer.setCreatedAt(user.getCreatedAt());
                Gallery gallery = new Gallery();
                gallery.setGalleryId(user.getUserId());
                gallery.setCreatedAt(user.getCreatedAt());
                influencerRepository.save(influencer);
                galleryRepository.save(gallery);
            } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID)) {
                Brand brand = new Brand();
                brand.setUserId(user.getUserId());
                brand.setCreatedAt(user.getCreatedAt());
                brandRepository.save(brand);
            } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.ADMIN_ROLE_ID)) {
                userRepository.deleteById(user.getUserId());
                return ResponseEntity.status(403).body(Map.of(
                        "error", "Access is denied."));
            }
        } else {
            return ApiResponse.sendError(404, "Role is not existed", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(201, "Account registered successfully", null, request.getRequestURI());
    }
    
    public ResponseEntity<?> registerAdmin(Admin admin, HttpServletRequest request) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            return ApiResponse.sendError(400, "Email is existed", request.getRequestURI());
        }
        admin.setPassword(JwtUtil.hashPassword(admin.getPassword()));
        adminRepository.save(admin);
        return ApiResponse.sendSuccess(201, "Account registered successfully", null, request.getRequestURI());
    }
    
    public ResponseEntity<?> loginAccount(User user, HttpServletRequest request) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (!existing.isPresent()) {
            return ApiResponse.sendError(404, "Email does not exist", request.getRequestURI());
        }
        if (!JwtUtil.isCorrectPassword(existing.get().getPassword(), user.getPassword())) {
            return ApiResponse.sendError(401, "Incorrect password", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Login successful",
                Map.of(
                        "token", JwtUtil.createToken(existing.get()),
                        "id", existing.get().getUserId()),
                request.getRequestURI());
    }
    
    public ResponseEntity<?> changeUserPassword(PasswordChange passwordRequest, HttpServletRequest request) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String userId = decodeJWT.getSubject();
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.sendError(401, "Invalid or expired token", request.getRequestURI());
        }
        User user = userOpt.get();
        if (!JwtUtil.isCorrectPassword(user.getPassword(), passwordRequest.getOldPassword())) {
            return ApiResponse.sendError(401, "Incorrect old password", request.getRequestURI());
        }
        
        if (!passwordRequest.getNewPassword().equalsIgnoreCase(passwordRequest.getPasswordConfirm())) {
            return ApiResponse.sendError(400, "New password and confirmation do not match", request.getRequestURI());
        }
        
        if (passwordRequest.getNewPassword().equalsIgnoreCase(passwordRequest.getOldPassword())) {
            return ApiResponse.sendError(400, "New password cannot be the same as the old password",
                    request.getRequestURI());
        }
        
        user.setPassword(JwtUtil.hashPassword(passwordRequest.getNewPassword()));
        
        return ApiResponse.sendSuccess(200, "Password changed successfully", null, request.getRequestURI());
    }
    
    public ResponseEntity<?> recoveryPasswordByEndpoint(String email, String url, HttpServletRequest request) {
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ApiResponse.sendError(400, "Invalid email", request.getRequestURI());
        }
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ApiResponse.sendError(400, "Email is existed", request.getRequestURI());
        }
        String resetURL = JwtUtil.createURLResetPassword(url, email);
//        String subject = "Reset your password";
//        String message = "Click this url: " + resetURL + " to reset your password.";
//      emailService.sendSimpleEmail(email, subject, message);
        emailService.sendResetPasswordEmail(email, resetURL);
        return ApiResponse.sendSuccess(200, "Password reset request sent successfully to your email", url, request.getRequestURI());
    }

    public ResponseEntity<?> resetPasswordByToken(String token, PasswordReset passwordReset, HttpServletRequest request) {
        try {
            User user;
            DecodedJWT decodeJWT = JwtUtil.decodeToken(token);
            user = userRepository.findByEmail(decodeJWT.getSubject()).get();
            if (!passwordReset.getPassword().equalsIgnoreCase(passwordReset.getPasswordConfirm())) {
                return ApiResponse.sendError(400, "New password and confirmation do not match",
                        request.getRequestURI());
            }
            user.setPassword(JwtUtil.hashPassword(passwordReset.getPassword()));
            userRepository.save(user);
            return ApiResponse.sendSuccess(200, "Password reset successfully", null, request.getRequestURI());
        } catch (Exception e) {
            return ApiResponse.sendError(401, "Invalid or expired token", request.getRequestURI());
        }
    }
    
}
