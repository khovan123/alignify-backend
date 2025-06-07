package com.api.controller;

import com.api.model.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneProfile(@PathVariable("id") String id, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.getProfileById(id, userDetails, request);
    }

    @PutMapping("")
    public ResponseEntity<?> updateProfile(@RequestBody Influencer influencer, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.updateProfileById(influencer, userDetails, request);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.deleteAccountById(userDetails, request);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> changeAvatar(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return profileService.saveAvatarUrlById(file, userDetails, request);
    }

}
