package com.api.controller;

import com.api.security.CustomUserDetails;
import com.api.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<?> getAllProfileByRoleId(@RequestParam("roleId") String roleId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.getAllProfileByRoleId(roleId, userDetails, request);
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.getMe(userDetails, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable("id") String id, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.getProfileById(id, userDetails, request);
    }

    @PutMapping("")
    public ResponseEntity<?> updateProfile(@RequestBody Object profile, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.updateProfile(profile, userDetails, request);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.deleteAccount(userDetails, request);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> changeAvatar(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.saveAvatarUrl(file, userDetails, request);
    }

}
