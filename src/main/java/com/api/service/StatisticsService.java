package com.api.service;

import com.api.dto.response.BrandStatisticsResponse;
import com.api.dto.response.InfluencerStatisticsResponse;

public interface StatisticsService {
    BrandStatisticsResponse getBrandStatistics(Long brandId);
    InfluencerStatisticsResponse getInfluencerStatistics(Long influencerId);
}
