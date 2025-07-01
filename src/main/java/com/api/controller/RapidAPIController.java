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
    public ResponseEntity<?> getUserStatsFromTiktok(@PathVariable("uniqueId") String uniqueId,
            HttpServletRequest request) {
        return rapidAPIService.getUserStatsFromTiktok(uniqueId, request);
    }

    @GetMapping("/tiktok/video/{videoId}/statsV2")
    public ResponseEntity<?> getVideoStatsFromTiktok(@PathVariable("videoId") String videoId,
            HttpServletRequest request) {
        return rapidAPIService.getVideoDetailsFromTiktok(videoId, request);
    }

    @GetMapping("/youtube/{channelName}/subscriber_count")
    public ResponseEntity<?> getSubcriberCountChannelFromYoutube(@PathVariable("channelName") String channelName,
            HttpServletRequest request) {
        return rapidAPIService.getStatsChannelFromYoutube(channelName, request);
    }

    @GetMapping("/youtube/{videoId}")
    public ResponseEntity<?> getVideoDetatilsFromYoutube(@PathVariable("videoID") String videoId,
            HttpServletRequest request) {
        return rapidAPIService.getVideoDetailsFromYoutube(videoId, request);
    }
}
