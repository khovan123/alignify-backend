package com.api.controller.websocket.gemini;

import com.api.dto.request.AssistantRequest;
import com.api.security.CustomUserDetails;
import com.api.service.GeminiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class GeminiController {
    @Autowired
    private GeminiService geminiService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/assistant/influencers/campaigns")
    public void getRecommendCampaigns(
            @Payload AssistantRequest assistantRequest,
            Principal principal) {
        messagingTemplate.convertAndSend("/topic/assistant/campaigns", geminiService.getCampaignRecommendations(assistantRequest,principal));
    }
}
