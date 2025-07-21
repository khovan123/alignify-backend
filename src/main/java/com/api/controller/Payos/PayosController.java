package com.api.controller.Payos;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.model.Plan;
import com.api.model.User;
import com.api.model.UserPlan;
import com.api.repository.PlanRepository;
import com.api.repository.UserPlanRepository;
import com.api.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

@RestController
@RequestMapping("/api/v1/payment")
public class PayosController {
    private final PayOS payOS;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserPlanRepository userPlanRepository;

    public PayosController(PayOS payOS) {
        super();
        this.payOS = payOS;

    }

    @PostMapping(path = "/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
        WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
        long orderCode = data.getOrderCode();
        UserPlan userPlan = userPlanRepository.findById(String.valueOf(orderCode)).get();
        boolean success;
        if ("00".equals(data.getCode()) || data.getDesc().toLowerCase().contains("thành công")) {
            userPlan.setStatus("SUCCESS");
            success = true;
        } else {
            userPlan.setStatus("FAILED");
            success = false;
        }
        userPlan.setCompletedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        userPlanRepository.save(userPlan);
        return response.put("success", success);
    }

}
