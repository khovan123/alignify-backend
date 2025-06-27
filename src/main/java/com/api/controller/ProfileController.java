package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.security.CustomUserDetails;
import com.api.service.ProfileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<?> getAllProfileByRoleId(
            @RequestParam("roleId") String roleId,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return profileService.getAllProfileByRoleId(roleId, pageNumber, pageSize, userDetails, request);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.getMe(userDetails, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable("id") String id,
            @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.getProfileById(id, userDetails, request);
    }

    @PutMapping("")
    public ResponseEntity<?> updateProfile(@RequestBody Object profile,
            @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.updateProfile(profile, userDetails, request);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return profileService.deleteAccount(userDetails, request);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> changeAvatar(@RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.saveAvatarUrl(image, userDetails, request);
    }

    @GetMapping("/topInfluencer")
    public ResponseEntity<?> getTopInfluencers(HttpServletRequest request) {
        return profileService.getTopInfluencers(request);
    }

    @PostMapping("/brands/search")
    public ResponseEntity<?> searchBrand(
            @RequestParam("term") String term,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        return profileService.searchBrandByTerm(term, pageNumber, pageSize, request);
    }

    @PostMapping("/influencers/search")
    public ResponseEntity<?> searchInfluencer(
            @RequestParam("term") String term,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        return profileService.searchInfluencerByTerm(term, pageNumber, pageSize, request);
    }

}
