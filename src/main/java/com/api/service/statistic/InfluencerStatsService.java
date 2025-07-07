package com.api.service.statistic;

import com.api.dto.statistic.InfluencerStatsResponse;

public interface InfluencerStatsService {
    InfluencerStatsResponse getInfluencerStats(String influencerId, int months);
}