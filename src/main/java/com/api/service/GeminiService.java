package com.api.service;

import com.api.config.EnvConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.dto.ApiResponse;
import com.api.dto.response.CampaignResponse;
import com.api.dto.response.InfluencerProfileResponse;
import com.api.model.Campaign;
import com.api.model.Influencer;
import com.api.model.User;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Value("${gemini.api.key}")
    private String apiKey;
    private Client client;

    @PostConstruct
    public void initGeminiClient() {
        this.client = Client.builder().apiKey(apiKey).build();
    }

    @Autowired
    private InfluencerRepository influencerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> getInfluencerRecommendations(String campaignId, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Optional<Campaign> campaignOpt = campaignRepository.findByCampaignIdAndBrandId(campaignId, brandId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Not found campaign with Id: " + campaignId, request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        try {
            List<User> users = userRepository.findByRoleIdAndUserIdNotIn(EnvConfig.INFLUENCER_ROLE_ID,
                    campaign.getJoinedInfluencerIds());
            List<String> userIds = users.stream().map(User::getUserId).toList();
            List<Influencer> influencers = influencerRepository.findAllById(userIds);
            Map<String, Influencer> influencerMap = influencers.stream()
                    .collect(Collectors.toMap(Influencer::getUserId, influencer -> influencer));

            List<InfluencerProfileResponse> influencerProfileResponses = users.stream().map(user -> {
                Influencer influencer = influencerMap.get(user.getUserId());
                return new InfluencerProfileResponse(user, influencer,
                        categoryRepository);
            }).toList();
            String prompt = "Based on campaign requirements: " + toJson(campaign)
                    + ", recommend influencers from: " + toJson(influencerProfileResponses)
                    + ". Return a JSON array of influencer profile response in: " + InfluencerProfileResponse.class;

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-1.5-flash",
                    prompt,
                    null);

            List<InfluencerProfileResponse> recommendedInfluencers = objectMapper.readValue(
                    response.text(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, InfluencerProfileResponse.class));
            Map<String, Object> data = new HashMap<>();
            data.put("recommendedInfluencers", recommendedInfluencers);

            return ApiResponse.sendSuccess(
                    200,
                    "Influencer recommendations retrieved successfully",
                    data,
                    request.getRequestURI());
        } catch (JsonProcessingException e) {
            return ApiResponse.sendError(
                    500,
                    "Failed to retrieve influencer recommendations: " + e.getMessage(),
                    request.getRequestURI());
        }
    }

    public ResponseEntity<?> getCampaignRecommendations(CustomUserDetails userDetails, HttpServletRequest request) {
        String influencerId = userDetails.getUserId();
        try {
            User user = userRepository.findById(influencerId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + influencerId));
            Influencer influencer = influencerRepository.findById(influencerId)
                    .orElseThrow(() -> new RuntimeException("Influencer not found with ID: " + influencerId));
            InfluencerProfileResponse influencerProfileResponse = new InfluencerProfileResponse(user, influencer,
                    categoryRepository);
            List<Campaign> campaigns = campaignRepository.findAllByStatusOrderByCreatedAtDesc("RECRUITING");
            List<String> brandIds = campaigns.stream()
                    .map(Campaign::getBrandId)
                    .distinct()
                    .toList();
            Map<String, User> brandsById = userRepository.findAllById(brandIds).stream()
                    .collect(Collectors.toMap(User::getUserId, u -> u));
            List<CampaignResponse> campaignResponses = campaigns.stream().map(campaign -> {
                User brand = Optional.ofNullable(brandsById.get(campaign.getBrandId()))
                        .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + campaign.getBrandId()));
                return new CampaignResponse(brand, campaign, categoryRepository);
            }).toList();
            String prompt = "Analyze campaign details: " + toJson(campaignResponses)
                    + ". Recommend campaigns for influencer: " + toJson(
                            influencerProfileResponse)
                    + ". Return a JSON array of campaign response in: " + CampaignResponse.class;

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-1.5-flash",
                    prompt,
                    null);

            List<CampaignResponse> recommendedCampaigns = objectMapper.readValue(
                    response.text(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CampaignResponse.class));
            Map<String, Object> data = new HashMap<>();
            data.put("recommendedCampaigns", recommendedCampaigns);

            return ApiResponse.sendSuccess(
                    200,
                    "Campaign recommendations retrieved successfully",
                    data,
                    request.getRequestURI());
        } catch (JsonProcessingException e) {
            return ApiResponse.sendError(
                    500,
                    "Failed to retrieve campaign recommendations: " + e.getMessage(),
                    request.getRequestURI());
        }
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
