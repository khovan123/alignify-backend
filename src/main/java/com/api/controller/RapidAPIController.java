package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.service.RapidAPIService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/v1/rapidapi")
public class RapidAPIController {

    @Autowired
    private RapidAPIService rapidAPIService;

    @GetMapping("/tiktok/user/{uniqueId}/statsV2")
    public ResponseEntity<?> getUserStatsFromTiktok(
            @PathVariable("uniqueId") String uniqueId,
            HttpServletRequest request) {
        return rapidAPIService.getUserStatsFromTiktok(uniqueId, request);
    }

    @GetMapping("/tiktok/video/{videoId}/statsV2")
    public ResponseEntity<?> getVideoStatsFromTiktok(
            @PathVariable("videoId") String videoId,
            HttpServletRequest request) {
        return rapidAPIService.getVideoDetailsFromTiktok(videoId, request);
    }

    @GetMapping("/youtube/{channelName}/subscriber_count")
    public ResponseEntity<?> getSubcriberCountChannelFromYoutube(@PathVariable("channelName") String channelName,
            HttpServletRequest request) {
        return rapidAPIService.getStatsChannelFromYoutube(channelName, request);
    }

    @GetMapping("/youtube/{videoId}")
    public ResponseEntity<?> getVideoDetatilsFromYoutube(
            @PathVariable("videoID") String videoId,
            HttpServletRequest request) {
        return rapidAPIService.getVideoDetailsFromYoutube(videoId, request);
    }

    @GetMapping("/facebook/page/{pageName}")
    public ResponseEntity<?> getPageStatsFromFacebook(
            @PathVariable("pageName") String pageName,
            HttpServletRequest request) {
        return rapidAPIService.getStatsPageFromFacebook(pageName, request);
    }

    @GetMapping("/facebook/post/{postId}")
    public ResponseEntity<?> getPostDetailsFromFacebook(
            @PathVariable("postId") String postId,
            HttpServletRequest request) {
        return rapidAPIService.getPostDetailsFromFacebook(postId, request);
    }

    @GetMapping("/instagram/user/${userName}")
    public ResponseEntity<?> getUserStatsFromInstagram(
            @PathVariable("userName") String userName,
            HttpServletRequest request) {
        return rapidAPIService.getStatsUserFromInstagram(userName, request);
    }

    @GetMapping("/instagram/post_or_reel_or_stories_or_TV_post/${code_or_id_or_url}")
    public ResponseEntity<?> getInforDetailsFromInstagram(
            @PathVariable("code_or_id_or_url") String code_or_id_or_url,
            HttpServletRequest request) {
        return rapidAPIService.getInforDetailsFromInstagram(code_or_id_or_url, request);
    }
}
