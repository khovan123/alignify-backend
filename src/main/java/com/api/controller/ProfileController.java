package com.api.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.api.config.*;
import com.api.model.*;
import com.api.repository.*;
import com.api.util.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/profile")
public class ProfileController {

    @Autowired
    private InfluencerRepository influencerProfileRepository;
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
    private ImageRepository imageRepository;

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> getAllProfile(@RequestParam String roleId, HttpServletRequest request) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String userId = decodeJWT.getSubject();
        List<Map<String, Object>> userList = new ArrayList<>();
        if (roleId.equalsIgnoreCase(EnvConfig.ADMIN_ROLE_ID)) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Access is denied."
            ));
        }
        userRepository.findByRoleIdAndUserIdNot(roleId, userId).forEach(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", user.getName());
            map.put("id", user.getUserId());
            if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
                Optional<Influencer> profileOtp = influencerProfileRepository.findById(user.getUserId());
                if (profileOtp.isPresent()) {
                    Influencer profile = profileOtp.get();
                    map.put("rating", profile.getRating());
                    map.put("avatarUrl", profile.getAvatarUrl());
                    map.put("isPublic", profile.isIsPublic());
                    map.put("followerIds", profile.getFollowerIds());
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

        return ResponseEntity.status(200).body(Map.of(
                "data", Map.of("profile", userList)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable("id") String id, HttpServletRequest request) {
        User user;
        try {
            user = userRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Profile not found."
            ));
        }
        Role roleOpt = roleRepository.findById(user.getRoleId()).get();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        if (user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            Optional<Influencer> profileOtp = influencerProfileRepository.findById(user.getUserId());
            if (profileOtp.isPresent()) {
                Influencer profile = profileOtp.get();
                map.put("bio", profile.getBio());
                map.put("rating", profile.getRating());
                map.put("avatarUrl", profile.getAvatarUrl());
                if (profile.getCategoryIds() != null) {
                    List<Category> categories = categoryRepository.findByCategoryIdIn(profile.getCategoryIds());
                    map.put("category", categories);
                }
                map.put("role", roleOpt.getRoleName());
                map.put("gender", profile.getGenderId());
                map.put("isPublic", profile.isIsPublic());
                map.put("followerIds", profile.getFollowerIds());
                if (profile.isIsPublic() || Helper.isOwner(id, request)) {
                    map.put("socialMediaLink", profile.getSocialMediaLinks());
                    map.put("DoB", profile.getDoB());
                    map.put("location", user.getLocation());
                    Optional<Gallery> galleryOpt = galleryRepository.findById(id);
                    if (galleryOpt.isPresent() && !galleryOpt.get().getImages().isEmpty() && galleryOpt.get().getImages() != null) {
                        List<String> imageIds = galleryOpt.get().getImages();
                        List<Image> images = imageRepository.findTop9ByIdInOrderByUploadedAtDesc(imageIds, PageRequest.of(0, 9)).stream()
                                .sorted(Comparator.comparing(Image::getCreateAt).reversed())
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
        return ResponseEntity.status(200).body(Map.of(
                "data", Map.of("profile", map)
        ));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Influencer newProfile, HttpServletRequest request) {
        if (!Helper.isOwner(id, request)) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Access is denied. You do not have permission to view this directory or page using the credentials that you supplied."
            ));
        }

        Influencer influencerProfile;
        try {
            influencerProfile = influencerProfileRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Profile not found."
            ));
        }

        Influencer profile = influencerProfile;

        if (newProfile.getBio() != null) {
            profile.setBio(newProfile.getBio());
        }

        if (newProfile.getAvatarUrl() != null && !newProfile.getAvatarUrl().isEmpty()) {
            profile.setAvatarUrl(newProfile.getAvatarUrl());
        }

        if (newProfile.getDoB() != null) {
            profile.setDoB(newProfile.getDoB());
        }

        if (newProfile.getGenderId() != null && !newProfile.getGenderId().isEmpty()) {
            profile.setGenderId(newProfile.getGenderId());
        }

        if (newProfile.getCategoryIds() != null) {
            profile.setCategoryIds(newProfile.getCategoryIds());
        }

        if (newProfile.getFollowerIds() != null) {
            profile.setFollowerIds(newProfile.getFollowerIds());
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
            influencerProfileRepository.save(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                    "error", e.getMessage()
            ));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "Profile updated."
        ));
    }

}
