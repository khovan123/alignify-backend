
package com.api.controller.Payos;


import com.api.model.Plan;
import com.api.model.User;
import com.api.model.UserPlan;
import com.api.repository.PlanRepository;
import com.api.repository.UserPlanRepository;
import com.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.payos.PayOS;
import vn.payos.type.Webhook;
import vn.payos.type.WebhookData;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/payment")
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
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        System.out.println(body);
        try {
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);

            if (
                    ("00".equals(data.getCode())) ||
                            (data.getDesc() != null && data.getDesc().toLowerCase().contains("thành công"))
            )
            {
                long orderCode = data.getOrderCode();
                String description = data.getDescription();

                // Parse description
                String[] parts = description.split("\\|");
                String userId = null;
                String planId = null;
                for (String part : parts) {
                    if (part.startsWith("userId:")) {
                        userId = part.replace("userId:", "").trim();
                    } else if (part.startsWith("planId:")) {
                        planId = part.replace("planId:", "").trim();
                    }
                }

                if (userId != null && planId != null) {
                    Optional<User> userOpt = userRepository.findById(userId);
                    Optional<Plan> planOpt = planRepository.findById(planId);

                    if (userOpt.isPresent() && planOpt.isPresent()) {
                        UserPlan userPlan = new UserPlan();
                        userPlan.setUserId(userId);
                        userPlan.setPlanId(planId);
                        userPlan.setOrderCode(orderCode);
                        userPlan.setCreatedAt(ZonedDateTime.now());

                        userPlanRepository.save(userPlan);
                        System.out.println("Saved UserPlan for userId=" + userId + ", planId=" + planId);
                    } else {
                        System.out.println("User or Plan not found with provided IDs.");
                    }
                } else {
                    System.out.println("Missing userId or planId in description: " + description);
                }

                ObjectNode dataNode = objectMapper.createObjectNode();
                dataNode.put("userId", userId);
                dataNode.put("planId", planId);

                response.put("error", 0);
                response.put("message", "Webhook delivered");
                response.set("data", dataNode);
                return response;

            }

            response.put("error", 1);
            response.put("message", "Payment not completed");
            response.set("data", null);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }
}
