package com.api.controller.websocket.gemini;

import com.api.dto.ApiResponse;
import com.api.model.AssistantMessage;
import com.api.repository.AssistantMessageRespository;
import com.api.security.CustomUserDetails;
import com.api.service.GeminiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assistant")
public class GeminiRestController {
    @Autowired
    private AssistantMessageRespository assistantMessageRespository;
    @Autowired
    private GeminiService geminiService;

    @GetMapping("/campaigns")
    private ResponseEntity<?> getAllAssistantMessage(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        List<AssistantMessage> assistantMessages = assistantMessageRespository.findAllByRoomId(userDetails.getUserId());
        return ApiResponse.sendSuccess(200, "Response successfully", assistantMessages, request.getRequestURI());
    }

    @GetMapping("/campaigns/{campaignId}/influencers")
    private ResponseEntity<?> getAllInfluencer(@PathVariable("campaignId") String campaignId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return geminiService.getInfluencerRecommendations(campaignId, userDetails, request);
    }
}
