package com.api.controller.websocket.gemini;

import com.api.dto.request.AssistantRequest;
import com.api.dto.response.AssistantResponse;
import com.api.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/assistant/influencers/{userId}")
    public void getRecommendCampaigns(
            @DestinationVariable String userId,
            @Payload AssistantRequest assistantRequest,
            Principal principal
    ) {
        AssistantResponse<?> response = geminiService.getCampaignRecommendations(userId, assistantRequest, principal);
        messagingTemplate.convertAndSend("/topic/assistant/influencers/" + userId, response);
    }
}