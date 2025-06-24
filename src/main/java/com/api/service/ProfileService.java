package com.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.request.BrandProfileRequest;
import com.api.dto.request.InfluencerProfileRequest;
import com.api.dto.response.BrandProfileResponse;
import com.api.dto.response.InfluencerProfileResponse;
import com.api.model.Application;
import com.api.model.Brand;
import com.api.model.Campaign;
import com.api.model.Influencer;
import com.api.model.Role;
import com.api.model.User;
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.RoleRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

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
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    public ResponseEntity<?> getAllProfileByRoleId(String roleId, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String userId = userDetails.getUserId();
        List<Map<String, Object>> userList = new ArrayList<>();
        if (roleId.equalsIgnoreCase(EnvConfig.ADMIN_ROLE_ID)) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }
        userRepository.findByRoleIdAndUserIdNot(roleId, userId).forEach(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", user.getName());
            map.put("id", user.getUserId());
            map.put("avatarUrl", user.getAvatarUrl());
            map.put("backgroundUrl", user.getBackgroundUrl());
            if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
                Optional<Influencer> profileOtp = influencerRepository.findById(user.getUserId());
                if (profileOtp.isPresent()) {
                    Influencer profile = profileOtp.get();
                    map.put("rating", profile.getRating());
                    map.put("isPublic", profile.isPublic());
                    map.put("follower", profile.getFollower());
                    if (!profile.getCategoryIds().isEmpty())
                        map.put("category", categoryRepository.findAllByCategoryIdIn(profile.getCategoryIds()));
                }
            } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID)) {
                Optional<Brand> profileOtp = brandRepository.findById(user.getUserId());
                if (profileOtp.isPresent()) {
                    Brand profile = profileOtp.get();
                    map.put("bio", profile.getBio());
                    map.put("contacts", profile.getContacts());
                    map.put("socialMediaLinks", profile.getSocialMediaLinks());
                    map.put("establishDate", profile.getEstablishDate());
                    map.put("totalCampaign", profile.getTotalCampaign());
                    if (!profile.getCategoryIds().isEmpty())
                        map.put("category", categoryRepository.findAllByCategoryIdIn(profile.getCategoryIds()));
                }
            }
            userList.add(map);
        });
        return ApiResponse.sendSuccess(200, "Response successfully", userList, request.getRequestURI());
    }

    public ResponseEntity<?> getMe(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getUserId();
        User user = userRepository.findById(id).get();
        Role role = roleRepository.findById(user.getRoleId()).get();
        if (role.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Influencer influencer = influencerRepository.findById(id).get();
            InfluencerProfileResponse influencerProfileReponse = new InfluencerProfileResponse(user, influencer,
                    getCompleteCampaign(id), true,
                    categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", influencerProfileReponse,
                    request.getRequestURI());
        } else if (role.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Brand brand = brandRepository.findById(id).get();
            BrandProfileResponse brandProfileResponse = new BrandProfileResponse(user, brand, categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", brandProfileResponse, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "Invalid roleId", request.getRequestURI());
    }

    public int getCompleteCampaign(String influencerId) {
        List<Application> applications = applicationRepository.findAllByInfluencerIdAndStatus(influencerId, "ACCEPTED");
        List<String> appliedCampaignIds = applications.stream()
                .map(Application::getCampaignId)
                .toList();
        List<Campaign> campaigns = campaignRepository.findAllByCampaignIdInAndStatus(appliedCampaignIds,
                "COMPLETED");
        return campaigns.size();
    }

    public ResponseEntity<?> getProfileById(String id, CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getUserId();
        User user = userRepository.findById(id).get();
        if (user.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Influencer influencer = influencerRepository.findById(id).get();

            InfluencerProfileResponse influencerProfileReponse = new InfluencerProfileResponse(user, influencer,
                    getCompleteCampaign(userId),
                    userId.equals(id), categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", influencerProfileReponse,
                    request.getRequestURI());
        } else if (user.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Brand brand = brandRepository.findById(id).get();
            BrandProfileResponse brandProfileResponse = new BrandProfileResponse(user, brand, categoryRepository);
            return ApiResponse.sendSuccess(200, "Response successfully", brandProfileResponse, request.getRequestURI());
        }
        return ApiResponse.sendError(400, "Invalid roleId", request.getRequestURI());
    }

    public ResponseEntity<?> updateProfile(Object profile, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String id = userDetails.getUserId();
        User user = userRepository.findById(id).get();
        if (user.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            Influencer influencer = influencerRepository.findById(id).get();
            // InfluencerProfileRequest newInfluencer = (InfluencerProfileRequest) profile;
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
            // BrandProfileRequest newBrand = (BrandProfileRequest) profile;
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
        String id = userDetails.getUserId();
        User user = userRepository.findById(id).get();
        user.setActive(false);
        userRepository.save(user);
        return ApiResponse.sendSuccess(204, null, null, request.getRequestURI());
    }

    public ResponseEntity<?> saveAvatarUrl(MultipartFile file, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String id = userDetails.getUserId();
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }
        String imageUrl;
        try {
            imageUrl = fileStorageService.storeFile(file);
        } catch (Exception e) {
            return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
        }
        userOpt.get().setAvatarUrl(imageUrl);

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
