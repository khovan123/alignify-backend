package com.api.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.UserDTO;
import com.api.dto.request.LoginRequest;
import com.api.dto.request.PasswordChangeRequest;
import com.api.dto.request.PasswordResetRequest;
import com.api.dto.request.RecoveryPasswordRequest;
import com.api.dto.request.RegisterRequest;
import com.api.dto.request.VerifyOTPRequest;
import com.api.model.AccountVerified;
import com.api.model.Admin;
import com.api.model.Brand;
import com.api.model.Gallery;
import com.api.model.Influencer;
import com.api.model.Permission;
import com.api.model.Role;
import com.api.model.User;
import com.api.model.UserBan;
import com.api.repository.AccountVerifiedRepository;
import com.api.repository.AdminRepository;
import com.api.repository.BrandRepository;
import com.api.repository.GalleryRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.PermissionRepository;
import com.api.repository.RoleRepository;
import com.api.repository.UserBanRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
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
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserBanRepository userBanRepository;
    @Value("${spring.google.client-id}")
    private String clientId;
    @Value("${spring.google.secret-key}")
    private String secretId;
    @Value("${spring.google.google-apis}")
    private String googleapis;
    @Value("${spring.google.preset}")
    private String preset;

    @SuppressWarnings("deprecation")
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
                    preset).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String avatar = (String) payload.get("picture");

            User user;
            if (!userRepository.existsByEmail(email)) {
                user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setRoleId(EnvConfig.INFLUENCER_ROLE_ID);
                user.setPassword(JwtUtil.hashPassword(email));
                user = userRepository.save(user);
                Influencer influencer = new Influencer();
                influencer.setUserId(user.getUserId());
                influencer.setCreatedAt(user.getCreatedAt());
                Gallery gallery = new Gallery();
                gallery.setGalleryId(user.getUserId());
                gallery.setCreatedAt(user.getCreatedAt());
                influencerRepository.save(influencer);
                galleryRepository.save(gallery);
            } else {
                user = userRepository.findByEmail(email).get();
            }
            Role role = roleRepository.findById(user.getRoleId()).get();
            UserDTO userDTO = new UserDTO(user.getUserId(), user.getName(), avatar);

            return ApiResponse.sendSuccess(200, "Login successful", Map.of(
                    "token", JwtUtil.createToken(user),
                    "role", role.getRoleName(),
                    "user", userDTO), request.getRequestURI());
        } catch (IOException e) {
            return ApiResponse.sendError(401, "Invalid Google authentication code", request.getRequestURI());
        }
    }

    public ResponseEntity<?> sendRequestOtpCode(String email, HttpServletRequest request) {
        try {
            if (accountVerifiedRepository.existsByEmail(email)) {
                return ApiResponse.sendError(400, "Email is existed", request.getRequestURI());
            }
            // emailService.sendSimpleEmail(email, "Your OTP Code", "Your OTP code is: " +
            // otpService.generateOtp(email) + ". It is valid for 3 minutes.");
            emailService.sendOtpEmail(email, otpService.generateOtp(email));
        } catch (RuntimeException e) {
            return ApiResponse.sendError(400, e.getMessage(), request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "OTP sent successfully to your email", null, request.getRequestURI());
    }

    public ResponseEntity<?> verifyOtpCode(VerifyOTPRequest verifyOTPRequest, HttpServletRequest request) {
        try {
            boolean isValid = otpService.verifyOtp(verifyOTPRequest.getEmail(), verifyOTPRequest.getOtp());
            if (isValid) {
                accountVerifiedRepository.save(new AccountVerified(verifyOTPRequest.getEmail()));
                return ApiResponse.sendSuccess(200, "OTP verified successfully", null, request.getRequestURI());
            } else {
                return ApiResponse.sendError(401, "Invalid OTP code", request.getRequestURI());
            }
        } catch (RuntimeException e) {
            return ApiResponse.sendError(400, e.getMessage(), request.getRequestURI());
        }
    }

    public ResponseEntity<?> registerAccount(RegisterRequest registerRequest, String roleId,
            HttpServletRequest request) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ApiResponse.sendError(400, "Email is existed", request.getRequestURI());
        }
        if (!accountVerifiedRepository.existsByEmail(registerRequest.getEmail())) {
            return ApiResponse.sendError(403, "Email verification required to complete registration",
                    request.getRequestURI());
        }
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            return ApiResponse.sendError(400, "Password and confirm password is wrong", request.getRequestURI());
        }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(JwtUtil.hashPassword(registerRequest.getPassword()));
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            user.setRoleId(roleId);
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

    public ResponseEntity<?> registerAdmin(RegisterRequest registerRequest, HttpServletRequest request) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ApiResponse.sendError(400, "Email is existed", request.getRequestURI());
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setRoleId(EnvConfig.ADMIN_ROLE_ID);
        user.setPassword(JwtUtil.hashPassword(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        Admin admin = new Admin();
        admin.setUserId(admin.getUserId());
        adminRepository.save(admin);
        userRepository.save(user);
        return ApiResponse.sendSuccess(201, "Account registered successfully", null, request.getRequestURI());
    }

    public ResponseEntity<?> loginAccount(LoginRequest loginRequest, HttpServletRequest request) {
        Optional<User> existing = userRepository.findByEmail(loginRequest.getEmail());
        if (!existing.isPresent()) {
            return ApiResponse.sendError(404, "Email does not exist", request.getRequestURI());
        }
        User user = existing.get();
        if (!JwtUtil.isCorrectPassword(existing.get().getPassword(), loginRequest.getPassword())) {
            return ApiResponse.sendError(401, "Incorrect password", request.getRequestURI());
        }
        Optional<Role> role = roleRepository.findById(user.getRoleId());
        if (!role.isPresent()) {
            return ApiResponse.sendError(400, "Invalid role", request.getRequestURI());
        }
        Optional<UserBan> userBanOpt = userBanRepository.findById(user.getUserId());
        if (userBanOpt.isPresent()) {
            return ApiResponse.sendError(403, "Your account has been banned", request.getRequestURI());
        }
        String avatarUrl = user.getAvatarUrl();
        List<Permission> permissions = permissionRepository.findByPermissionIdIn(user.getPermissionIds());
        UserDTO userDTO = new UserDTO(user.getUserId(), user.getName(), avatarUrl, permissions);
        if (existing.get().getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Optional<Influencer> influencerOpt = influencerRepository.findById(user.getUserId());
            if (!influencerOpt.isPresent()) {
                Influencer influencer = new Influencer();
                influencer.setUserId(user.getUserId());
                influencer.setCreatedAt(user.getCreatedAt());
                influencerRepository.save(influencer);
            }
        } else if (existing.get().getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Optional<Brand> brandOpt = brandRepository.findById(user.getUserId());
            if (!brandOpt.isPresent()) {
                Brand brand = new Brand();
                brand.setUserId(user.getUserId());
                brand.setCreatedAt(user.getCreatedAt());
                brandRepository.save(brand);
            }
        } else if (existing.get().getRoleId().equals(EnvConfig.ADMIN_ROLE_ID)) {
            Optional<Admin> adminOpt = adminRepository.findById(user.getUserId());
            if (!adminOpt.isPresent()) {
                Admin admin = new Admin();
                admin.setUserId(user.getUserId());
                admin.setCreatedAt(user.getCreatedAt());
                adminRepository.save(admin);
            }
        }
        return ApiResponse.sendSuccess(200, "Login successful", Map.of(
                "token", JwtUtil.createToken(existing.get()),
                "role", role.get().getRoleName(),
                "user", userDTO), request.getRequestURI());
    }

    public ResponseEntity<?> changeUserPassword(PasswordChangeRequest passwordRequest, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String userId = userDetails.getUserId();
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
        userRepository.save(user);
        return ApiResponse.sendSuccess(200, "Password changed successfully", null, request.getRequestURI());
    }

    public ResponseEntity<?> recoveryPasswordByEndpoint(RecoveryPasswordRequest recoveryPasswordRequest,
            HttpServletRequest request) {
        if (!recoveryPasswordRequest.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ApiResponse.sendError(400, "Invalid email", request.getRequestURI());
        }
        Optional<User> user = userRepository.findByEmail(recoveryPasswordRequest.getEmail());
        if (!user.isPresent()) {
            return ApiResponse.sendError(400, "Email is not existed", request.getRequestURI());
        }
        String resetURL = JwtUtil.createURLResetPassword(recoveryPasswordRequest.getUrl(),
                recoveryPasswordRequest.getEmail());
        // String subject = "Reset your password";
        // String message = "Click this url: " + + " to reset your password.";
        emailService.sendResetPasswordEmail(recoveryPasswordRequest.getEmail(), user.get().getName(),
                user.get().getAvatarUrl(), resetURL);
        // emailService.sendResetPasswordEmail(recoveryPasswordRequest.getEmail(),
        // resetURL);
        return ApiResponse.sendSuccess(200, "Password reset request sent successfully to your email", null,
                request.getRequestURI());
    }

    public ResponseEntity<?> resetPasswordByToken(String token, PasswordResetRequest passwordReset,
            HttpServletRequest request) {
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
