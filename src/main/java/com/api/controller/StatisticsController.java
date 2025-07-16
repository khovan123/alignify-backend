package com.api.controller;

import com.api.dto.response.BrandStatisticsResponse;
import com.api.dto.response.InfluencerStatisticsResponse;
import com.api.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/brand")
    public BrandStatisticsResponse getBrandStatistics(@RequestParam("brandId") String brandId) {
        try {
            return statisticsService.getBrandStatistics(brandId);
        } catch (Exception e) {
            throw new RuntimeException("Internal Server Error: " + e.getMessage(), e);
        }
    }

    @GetMapping("/influencer")
    public InfluencerStatisticsResponse getInfluencerStatistics(@RequestParam("influencerId") String influencerId) {
        try {
            return statisticsService.getInfluencerStatistics(influencerId);
        } catch (Exception e) {
            throw new RuntimeException("Internal Server Error: " + e.getMessage(), e);
        }
    }
}
