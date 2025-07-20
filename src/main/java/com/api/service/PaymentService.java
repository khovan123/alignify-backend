package com.api.service;

import com.api.model.UserPlan;
import com.api.repository.UserPlanRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.time.ZonedDateTime;

@Service
public class PaymentService {

    @Autowired
    private UserPlanRepository userPlanRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void processPayOSWebhook(ObjectNode body) throws JsonProcessingException {
        // Parse ObjectNode thành Webhook Java Object
        Webhook webhook = objectMapper.treeToValue(body, Webhook.class);
        WebhookData data = webhook.getData();  // <-- chứa thông tin payment

        if (data == null || !"00".equals(data.getCode())) {
            throw new IllegalStateException("Payment not successful or data is null");
        }

        long orderCode = data.getOrderCode();
        String planId = data.getDescription();
        String userId = data.getReference();

        // Tạo bản ghi UserPlan
        UserPlan userPlan = new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setPlanId(planId);
        userPlan.setCreatedAt(ZonedDateTime.now());
        userPlan.setOrderCode(orderCode);

        userPlanRepository.save(userPlan);
    }
}
