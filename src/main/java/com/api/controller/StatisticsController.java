package com.api.controller;

import com.api.dto.response.BrandStatisticsResponse;
import com.api.dto.response.InfluencerStatisticsResponse;
import com.api.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/brand")
    public BrandStatisticsResponse getBrandStatistics(@RequestParam Long brandId) {
        return statisticsService.getBrandStatistics(brandId);
    }

    @GetMapping("/influencer")
    public InfluencerStatisticsResponse getInfluencerStatistics(@RequestParam Long influencerId) {
        try {
            return statisticsService.getInfluencerStatistics(influencerId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Internal Server Error: " + e.getMessage(), e);
        }
    }
}
