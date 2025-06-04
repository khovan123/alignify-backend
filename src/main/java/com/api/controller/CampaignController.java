
package com.api.controller;

import com.api.model.Campaign;
import com.api.service.CampaignService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v1/campaign")
public class CampaignController {
    @Autowired
    private CampaignService campaignService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Campaign campaign, HttpServletRequest request) {
        return campaignService.createCampaign(campaign, request);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts(HttpServletRequest request) {
        return campaignService.getAllCampaign(request);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String userId, HttpServletRequest request) {
        return campaignService.getCampaignsById(userId, request);
    }

    @PutMapping("/{campaignId}/{userId}")
    public ResponseEntity<?> updatePost(@PathVariable String campaignId,
            @PathVariable String userId,
            @RequestBody Campaign campaign,
            HttpServletRequest request) {
        return campaignService.updateCampaign(campaignId,userId, campaign, request);
    }

    @DeleteMapping("/{campaignId}/{userId}")
    public ResponseEntity<?> deletePost(
            @PathVariable String campaignId,
            @PathVariable String userId,
            HttpServletRequest request) {

        return campaignService.deleteCampaign(campaignId,userId, request);
    }
    
  

}
