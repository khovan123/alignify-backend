package com.api.service;

import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.BrandProfileRequest;
import com.api.dto.BrandProfileResponse;
import com.api.dto.InfluencerProfileRequest;
import com.api.dto.InfluencerProfileResponse;
import com.api.model.*;
import com.api.repository.*;
import com.api.security.CustomUserDetails;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Service
public class ProfileService {

    @Autowired
    private InfluencerRepository influencerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Value("${cloudinary.upload-preset}")
    private String uploadPreset;

    public ResponseEntity<?> getAllProfileByRoleId(String roleId, CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getId();
        List<Map<String, Object>> userList = new ArrayList<>();
        if (roleId.equalsIgnoreCase(EnvConfig.ADMIN_ROLE_ID)) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }
        userRepository.findByRoleIdAndUserIdNot(roleId, userId).forEach(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", user.getName());
            map.put("id", user.getUserId());
            if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
                Optional<Influencer> profileOtp = influencerRepository.findById(user.getUserId());
                if (profileOtp.isPresent()) {
                    Influencer profile = profileOtp.get();
                    map.put("rating", profile.getRating());
                    map.put("avatarUrl", profile.getAvatarUrl());
                    map.put("isPublic", profile.isPublic());
                    map.put("follower", profile.getFollower());
                }
            } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID)) {
                Optional<Brand> profileOtp = brandRepository.findById(user.getUserId());
                if (profileOtp.isPresent()) {
                    Brand profile = profileOtp.get();
                    map.put("avatarUrl", profile.getAvatarUrl());
                }
            }
            userList.add(map);
        });
        return ApiResponse.sendSuccess(200, "Response successfully", userList, request.getRequestURI());
    }

    public ResponseEntity<?> getMe(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getId();
        User user = userRepository.findById(id).get();
        Role role = roleRepository.findById(user.getRoleId()).get();
        if (role.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Influencer influencer = influencerRepository.findById(id).get();
            InfluencerProfileResponse influencerProfileReponse = new InfluencerProfileResponse(user, influencer, true, categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", influencerProfileReponse, request.getRequestURI());
        } else if (role.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Brand brand = brandRepository.findById(id).get();
            BrandProfileResponse brandProfileResponse = new BrandProfileResponse(user, brand, categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", brandProfileResponse, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "Invalid roleId", request.getRequestURI());
    }

    public ResponseEntity<?> getProfileById(String id, CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getId();
        User user = userRepository.findById(id).get();
        if (user.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Influencer influencer = influencerRepository.findById(id).get();
            InfluencerProfileResponse influencerProfileReponse = new InfluencerProfileResponse(user, influencer, userId.equals(id), categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", influencerProfileReponse, request.getRequestURI());
        } else if (user.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Brand brand = brandRepository.findById(id).get();
            BrandProfileResponse brandProfileResponse = new BrandProfileResponse(user, brand, categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", brandProfileResponse, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "Invalid roleId", request.getRequestURI());
    }

    public ResponseEntity<?> updateProfile(Object profile, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String id = userDetails.getId();
        User user = userRepository.findById(id).get();
        if (user.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Influencer influencer = influencerRepository.findById(id).get();
//            InfluencerProfileRequest newInfluencer = (InfluencerProfileRequest) profile;
            InfluencerProfileRequest newInfluencer = convertToInfluencerProfileRequest(profile);
            if (newInfluencer.getBio() != null) {
                influencer.setBio(newInfluencer.getBio());
            }

            if (newInfluencer.getDoB() != null) {
                influencer.setDoB(newInfluencer.getDoB());
            }

            if (newInfluencer.getGender() != null) {
                influencer.setGender(newInfluencer.getGender().toUpperCase());
            }

            if (newInfluencer.getCategoryIds() != null && !newInfluencer.getCategoryIds().isEmpty()) {
                influencer.setCategoryIds(newInfluencer.getCategoryIds());
            }

            if (newInfluencer.getSocialMediaLinks() != null && !newInfluencer.getSocialMediaLinks().isEmpty()) {
                influencer.setSocialMediaLinks(newInfluencer.getSocialMediaLinks());
            }
            influencerRepository.save(influencer);
            return ApiResponse.sendSuccess(200, "Update successfully", influencer, request.getRequestURI());
        } else if (user.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Brand brand = brandRepository.findById(id).get();
//            BrandProfileRequest newBrand = (BrandProfileRequest) profile;
            BrandProfileRequest newBrand = convertToBrandProfileRequest(profile);
            if (newBrand.getBio() != null) {
                brand.setBio(newBrand.getBio());
            }
            if (newBrand.getContacts() != null && !newBrand.getContacts().isEmpty()) {
                brand.setCategoryIds(newBrand.getCategoryIds());
            }
            if (newBrand.getSocialMediaLinks() != null && !newBrand.getSocialMediaLinks().isEmpty()) {
                brand.setSocialMediaLinks(newBrand.getSocialMediaLinks());
            }
            if (newBrand.getEstablishDate() != null) {
                brand.setEstablishDate(newBrand.getEstablishDate());
            }
            if (newBrand.getCategoryIds() != null && !newBrand.getCategoryIds().isEmpty()) {
                brand.setCategoryIds(newBrand.getCategoryIds());
            }
            brandRepository.save(brand);
            return ApiResponse.sendSuccess(200, "Update successfully", brand, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "Invalid roleId", request.getRequestURI());
    }

    public ResponseEntity<?> deleteAccount(CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getId();
        User user = userRepository.findById(id).get();
        user.setActive(false);
        userRepository.save(user);
        return ApiResponse.sendSuccess(204, null, null, request.getRequestURI());
    }

    public ResponseEntity<?> saveAvatarUrl(MultipartFile file, CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getId();
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }
        String imageUrl = null;
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("upload_preset", uploadPreset));
            imageUrl = (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            return ApiResponse.sendError(500, "Internal server error", request.getRequestURI());
        }
        if (imageUrl == null) {
            return ApiResponse.sendError(500, "Internal server error", request.getRequestURI());
        }
        if (userOpt.get().getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            Optional<Influencer> influencerOpt = influencerRepository.findById(id);
            Influencer influencer = influencerOpt.get();
            influencer.setAvatarUrl(imageUrl);
            influencerRepository.save(influencer);
        } else if (userOpt.get().getRoleId().equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID)) {
            Optional<Brand> brandOpt = brandRepository.findById(id);
            Brand brand = brandOpt.get();
            brand.setAvatarUrl(imageUrl);
            brandRepository.save(brand);
        } else {
            return ApiResponse.sendError(400, "Invalid roleId", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Change avatar successfully", imageUrl, request.getRequestURI());
    }

    private InfluencerProfileRequest convertToInfluencerProfileRequest(Object profile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(profile, InfluencerProfileRequest.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert to InfluencerProfileRequest: " + e.getMessage());
        }
    }

    private BrandProfileRequest convertToBrandProfileRequest(Object profile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(profile, BrandProfileRequest.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert to BrandProfileRequest: " + e.getMessage());
        }
    }
}
