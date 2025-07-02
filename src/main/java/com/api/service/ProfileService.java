package com.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private FileStorageService fileStorageService;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public ResponseEntity<?> getAllProfileByRoleId(String roleId, int pageNumber, int pageSize,
            CustomUserDetails userDetails,
            HttpServletRequest request) {
        String userId = userDetails.getUserId();
        PageRequest page = PageRequest.of(pageNumber, pageSize);
        List<Map<String, Object>> userList = new ArrayList<>();
        if (roleId.equalsIgnoreCase(EnvConfig.ADMIN_ROLE_ID)) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }
        userRepository.findByRoleIdAndUserIdNotAndIsActiveTrue(roleId, userId, page).forEach(user -> {
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
                    if (!profile.getCategoryIds().isEmpty()) {
                        map.put("category", categoryRepository.findAllByCategoryIdIn(profile.getCategoryIds()));
                    }
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
                    if (!profile.getCategoryIds().isEmpty()) {
                        map.put("category", categoryRepository.findAllByCategoryIdIn(profile.getCategoryIds()));
                    }
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
            if (newInfluencer.getName() != null) {
                user.setName(newInfluencer.getName());
            }
            if (newInfluencer.getBio() != null) {
                influencer.setBio(newInfluencer.getBio());
            }

            if (newInfluencer.getDoB() != null) {
                influencer.setDoB(newInfluencer.getDoB());
            }

            if (newInfluencer.getGender() != null) {
                influencer.setGender(newInfluencer.getGender().toUpperCase());
            }
            if (newInfluencer.getIsPublic() != null) {
                influencer.setPublic(newInfluencer.getIsPublic());
            }

            if (newInfluencer.getCategoryIds() != null && !newInfluencer.getCategoryIds().isEmpty()) {
                influencer.setCategoryIds(newInfluencer.getCategoryIds());
            }

            if (newInfluencer.getSocialMediaLinks() != null && !newInfluencer.getSocialMediaLinks().isEmpty()) {
                influencer.setSocialMediaLinks(newInfluencer.getSocialMediaLinks());
            }
            userRepository.save(user);
            influencerRepository.save(influencer);
            InfluencerProfileResponse influencerProfile = new InfluencerProfileResponse(user, influencer,
                    categoryRepository);
            return ApiResponse.sendSuccess(200, "Update successfully", influencerProfile, request.getRequestURI());
        } else if (user.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            Brand brand = brandRepository.findById(id).get();
            // BrandProfileRequest newBrand = (BrandProfileRequest) profile;
            BrandProfileRequest newBrand = convertToBrandProfileRequest(profile);
            if (newBrand.getName() != null) {
                user.setName(newBrand.getName());
            }
            if (newBrand.getBio() != null) {
                brand.setBio(newBrand.getBio());
            }
            if (newBrand.getContacts() != null && !newBrand.getContacts().isEmpty()) {
                brand.setContacts(newBrand.getContacts());
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
            userRepository.save(user);
            brandRepository.save(brand);
            BrandProfileResponse brandProfile = new BrandProfileResponse(user, brand, categoryRepository);
            return ApiResponse.sendSuccess(200, "Update successfully", brandProfile, request.getRequestURI());
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
        User user = userOpt.get();
        String imageUrl;
        try {
            imageUrl = fileStorageService.storeFile(file);
        } catch (Exception e) {
            return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
        }
        user.setAvatarUrl(imageUrl);
        userRepository.save(user);
        return ApiResponse.sendSuccess(200, "Change avatar successfully", imageUrl, request.getRequestURI());
    }

    public ResponseEntity<?> getTopInfluencers(HttpServletRequest request) {
        List<Influencer> influencers = influencerRepository.findTop2ByOrderByFollowerDesc();

        Set<String> userIds = influencers.stream()
                .map(Influencer::getUserId)
                .collect(Collectors.toSet());

        Map<String, User> userMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<InfluencerProfileResponse> responseList = influencers.stream()
                .map(influencer -> {
                    User user = userMap.get(influencer.getUserId());
                    if (user == null) {
                        throw new IllegalStateException("User not found for influencerId: " + influencer.getUserId());
                    }
                    return new InfluencerProfileResponse(user, influencer, 0, true, categoryRepository);
                })
                .toList();

        return ApiResponse.sendSuccess(200, "Top influencers retrieved successfully", responseList,
                request.getRequestURI());
    }

    public ResponseEntity<?> searchBrandByTerm(String term, int pageNumber, int pageSize, HttpServletRequest request) {
        if (term == null || term.trim().isEmpty()) {
            return ApiResponse.sendSuccess(200, "No brands found", Collections.emptyList(), request.getRequestURI());
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> brandPage = userRepository.findByNameContainingIgnoreCaseAndRoleId(
                term, EnvConfig.BRAND_ROLE_ID, pageable);

        List<Map<String, Object>> brandList = brandPage.getContent().stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getUserId());
            map.put("name", user.getName());
            map.put("avatarUrl", user.getAvatarUrl());
            map.put("backgroundUrl", user.getBackgroundUrl());
            Optional<Brand> brandOpt = brandRepository.findById(user.getUserId());
            if (brandOpt.isPresent()) {
                Brand brand = brandOpt.get();
                map.put("bio", brand.getBio());
                map.put("contacts", brand.getContacts());
                map.put("socialMediaLinks", brand.getSocialMediaLinks());
                map.put("establishDate", brand.getEstablishDate());
                map.put("totalCampaign", brand.getTotalCampaign());
            }
            return map;
        }).toList();

        return ApiResponse.sendSuccess(200, "Search brand success", brandList, request.getRequestURI());
    }

    public ResponseEntity<?> searchInfluencerByTerm(String term, int pageNumber, int pageSize,
            HttpServletRequest request) {
        if (term == null || term.trim().isEmpty()) {
            return ApiResponse.sendSuccess(200, "No influencers found", Collections.emptyList(),
                    request.getRequestURI());
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> influencerPage = userRepository.findByNameContainingIgnoreCaseAndRoleId(
                term, EnvConfig.INFLUENCER_ROLE_ID, pageable);

        List<Map<String, Object>> influencerList = influencerPage.getContent().stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getUserId());
            map.put("name", user.getName());
            map.put("avatarUrl", user.getAvatarUrl());
            map.put("backgroundUrl", user.getBackgroundUrl());
            Optional<Influencer> influencerOpt = influencerRepository.findById(user.getUserId());
            if (influencerOpt.isPresent()) {
                Influencer influencer = influencerOpt.get();
                map.put("bio", influencer.getBio());
                map.put("follower", influencer.getFollower());
                map.put("rating", influencer.getRating());
                map.put("isPublic", influencer.isPublic());
                map.put("gender", influencer.getGender());
                map.put("DoB", influencer.getDoB());
                if (influencer.getCategoryIds() != null && !influencer.getCategoryIds().isEmpty()) {
                    map.put("category", categoryRepository.findAllByCategoryIdIn(influencer.getCategoryIds()));
                }
                map.put("socialMediaLinks", influencer.getSocialMediaLinks());
            }
            return map;
        }).toList();

        return ApiResponse.sendSuccess(200, "Search influencer success", influencerList, request.getRequestURI());
    }

    private InfluencerProfileRequest convertToInfluencerProfileRequest(Object profile) {
        try {
            return objectMapper.convertValue(profile, InfluencerProfileRequest.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert to InfluencerProfileRequest: " + e.getMessage());
        }
    }

    private BrandProfileRequest convertToBrandProfileRequest(Object profile) {
        try {
            return objectMapper.convertValue(profile, BrandProfileRequest.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert to BrandProfileRequest: " + e.getMessage());
        }
    }

}