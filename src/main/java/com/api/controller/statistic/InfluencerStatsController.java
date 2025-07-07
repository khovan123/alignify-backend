package com.api.controller.statistic;

import com.api.dto.statistic.InfluencerStatsResponse;
import com.api.service.statistic.InfluencerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats/influencer")
public class InfluencerStatsController {

    private final InfluencerStatsService statsService;

    @Autowired
    public InfluencerStatsController(InfluencerStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public ResponseEntity<InfluencerStatsResponse> getStats(
            @RequestParam(name = "influencerId") String influencerId,
            @RequestParam(name = "months", defaultValue = "6") int months) {
        InfluencerStatsResponse response = statsService.getInfluencerStats(influencerId, months);
        return ResponseEntity.ok(response);
    }
}