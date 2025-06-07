package com.api.controller;

import com.api.model.*;
import com.api.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Campaign campaign, HttpServletRequest request) {
        return campaignService.createCampaign(campaign, request);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getAllCampaign(page, size, request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCampaignsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getCampaignsById(userId, page, size, request);
    }

    @PutMapping("/{userId}/{campaignId}")
    public ResponseEntity<?> updatePost(@PathVariable String campaignId,
            @PathVariable String userId,
            @RequestBody Campaign campaign,
            HttpServletRequest request) {
        return campaignService.updateCampaign(campaignId, userId, campaign, request);
    }

    @DeleteMapping("/{userId}/{campaignId}")
    public ResponseEntity<?> deletePost(
            @PathVariable String campaignId,
            @PathVariable String userId,
            HttpServletRequest request) {

        return campaignService.deleteCampaign(campaignId, userId, request);
    }

}
