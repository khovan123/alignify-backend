package com.api.controller;

import com.api.model.*;
import com.api.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> getAllProfile(@RequestParam("roleId") String roleId, HttpServletRequest request) {
        return profileService.getAllProfileByRoleId(roleId, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneProfile(@PathVariable("id") String id, HttpServletRequest request) {
        return profileService.getProfileById(id, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable("id") String id, @RequestBody Influencer influencer, HttpServletRequest request) {
        return profileService.updateProfileById(id, influencer, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") String id, HttpServletRequest request) {
        return profileService.deleteAccountById(id, request);
    }

    @PostMapping("/avatar/{id}")
    public ResponseEntity<?> changeAvatar(@PathVariable("id") String id, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return profileService.saveAvatarUrlById(id, file, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") String id, HttpServletRequest request) {
        if (!Helper.isOwner(id, request)) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Access is denied"
            ));
        }
        User user = userRepository.findById(id).get();
        user.setIsActive(false);
        userRepository.save(user);
        return ResponseEntity.status(204).body(Map.of(
                "message", "Delete account successful"
        ));
    }

}
