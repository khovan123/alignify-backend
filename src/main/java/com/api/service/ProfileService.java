package com.api.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.model.*;
import com.api.repository.*;
import com.api.security.CustomUserDetails;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

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
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryImageRepository imageRepository;
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

    public ResponseEntity<?> getProfileById(String id, CustomUserDetails userDetails, HttpServletRequest request) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }
        String userId = userDetails.getId();
        User user = userOpt.get();
        Role roleOpt = roleRepository.findById(user.getRoleId()).get();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            Optional<Influencer> profileOtp = influencerRepository.findById(user.getUserId());
            if (profileOtp.isPresent()) {
                Influencer profile = profileOtp.get();
                map.put("bio", profile.getBio());
                map.put("rating", profile.getRating());
                map.put("avatarUrl", profile.getAvatarUrl());
                if (profile.getCategoryIds() != null) {
                    List<Category> categories = categoryRepository.findAllByCategoryIdIn(profile.getCategoryIds());
                    map.put("category", categories);
                }
                map.put("role", roleOpt.getRoleName());
                map.put("gender", profile.getGender());
                map.put("isPublic", profile.isPublic());
                map.put("followerIds", profile.getFollower());
                if (profile.isPublic() || userId.equals(id)) {
                    map.put("socialMediaLink", profile.getSocialMediaLinks());
                    map.put("DoB", profile.getDoB());
                    map.put("location", user.getLocation());
                    Optional<Gallery> galleryOpt = galleryRepository.findById(id);
                    if (galleryOpt.isPresent() && !galleryOpt.get().getImages().isEmpty()
                            && galleryOpt.get().getImages() != null) {
                        List<String> imageIds = galleryOpt.get().getImages();
                        List<GalleryImage> images = imageRepository
                                .findTop9ByIdInOrderByUploadedAtDesc(imageIds, PageRequest.of(0, 9)).stream()
                                .sorted(Comparator.comparing(GalleryImage::getCreatedAt).reversed())
                                .limit(9)
                                .collect(Collectors.toList());
                        if (!images.isEmpty()) {
                            map.put("gallery", images);
                        }
                    }
                }
            }
        } else if (user.getRoleId().equalsIgnoreCase(EnvConfig.BRAND_ROLE_ID)) {
            Optional<Brand> profileOtp = brandRepository.findById(user.getUserId());
            if (profileOtp.isPresent()) {
                Brand profile = profileOtp.get();
                map.put("bio", profile.getBio());
                map.put("avatarUrl", profile.getAvatarUrl());
                map.put("contact", profile.getContacts());
                map.put("socialMediaLink", profile.getSocialMediaLinks());
                map.put("establishDate", profile.getEstablishDate());
                map.put("location", user.getLocation());
            }
        }
        return ApiResponse.sendSuccess(200, "Response successfully", map, request.getRequestURI());
    }

    public ResponseEntity<?> updateProfileById(@RequestBody Influencer newProfile, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String id = userDetails.getId();
        Optional<Influencer> influencerOpt = influencerRepository.findById(id);
        if (!influencerOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }

        Influencer profile = influencerOpt.get();

        if (newProfile.getBio() != null) {
            profile.setBio(newProfile.getBio());
        }

        if (newProfile.getAvatarUrl() != null) {
            profile.setAvatarUrl(newProfile.getAvatarUrl());
        }

        if (newProfile.getDoB() != null) {
            profile.setDoB(newProfile.getDoB());
        }

        if (newProfile.getGender() != null) {
            profile.setGender(newProfile.getGender().toUpperCase());
        }

        if (newProfile.getCategoryIds() != null) {
            profile.setCategoryIds(newProfile.getCategoryIds());
        }

        if (newProfile.getSocialMediaLinks() != null) {
            Map<String, String> socialMediaLinks = profile.getSocialMediaLinks();
            if (socialMediaLinks == null) {
                socialMediaLinks = new HashMap<>();
            }
            for (Map.Entry<String, String> entry : newProfile.getSocialMediaLinks().entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isBlank()) {
                    socialMediaLinks.put(entry.getKey(), entry.getValue());
                }
            }
            profile.setSocialMediaLinks(socialMediaLinks);
        }

        try {
            influencerRepository.save(profile);
        } catch (Exception e) {
            return ApiResponse.sendError(500, "Internal server error", request.getRequestURI());
        }

        return ApiResponse.sendSuccess(200, "Update successfully", profile, id);
    }

    public ResponseEntity<?> deleteAccountById(CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getId();
        User user = userRepository.findById(id).get();
        user.setIsActive(false);
        userRepository.save(user);
        return ApiResponse.sendSuccess(204, null, null, request.getRequestURI());
    }

    public ResponseEntity<?> saveAvatarUrlById(MultipartFile file, CustomUserDetails userDetails, HttpServletRequest request) {
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
        }
        return ApiResponse.sendSuccess(200, "Change avatar successfully", imageUrl, request.getRequestURI());
    }
}
