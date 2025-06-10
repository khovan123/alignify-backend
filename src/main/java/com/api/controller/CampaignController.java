package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Campaign;
import com.api.security.CustomUserDetails;
import com.api.service.CampaignService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("api/v1/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody Campaign campaign,@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return campaignService.createCampaign(campaign,userDetails, request);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getAllCampaign(page, size, request);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getMe(userDetails, page, size, request);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCampaignsById(
            @PathVariable("userId") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getCampaignsById(userId, page, size, request);
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<?> updatePost(@PathVariable String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Campaign campaign,
            HttpServletRequest request) {
        return campaignService.updateCampaign(campaignId, userDetails, campaign, request);
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<?> deletePost(
            @PathVariable String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        return campaignService.deleteCampaign(campaignId, userDetails, request);
    }

}
